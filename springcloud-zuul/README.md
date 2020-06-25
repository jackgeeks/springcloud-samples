## 为什么需要网关呢？

我们知道我们要进入一个服务本身，很明显我们没有特别好的办法，直接输入IP地址+端口号，我们知道这样的做法很糟糕的，这样的做法大有问题，首先暴露了我们实体机器的IP地址，别人一看你的IP地址就知道服务部署在哪里，让别人很方便的进行攻击操作。

第二，我们这么多服务，我们是不是要挨个调用它呀，我们这里假设做了个权限认证，我们每一个客户访问的都是跑在不同机器上的不同的JVM上的服务程序，我们每一个服务都需要一个服务认证，这样做烦不烦呀，明显是很烦的。

那么我们这时候面临着这两个极其重要的问题，这时我们就需要一个办法解决它们。首先，我们看IP地址的暴露和IP地址写死后带来的单点问题，我是不是对这么服务本身我也要动态的维护它服务的列表呀，我需要调用这服务本身，是不是也要一个负载均衡一样的玩意，

还有关于IP地址暴露的玩意，我是不是需要做一个代理呀，像Nginx的反向代理一样的东西，还有这玩意上部署公共的模块，比如所有入口的权限校验的东西。因此我们现在需要Zuul API网关。它就解决了上面的问题，你想调用某个服务，它会给你映射，把你服务的IP地址映射成

某个路径，你输入该路径，它匹配到了，它就去替你访问这个服务，它会有个请求转发的过程，像Nginx一样，服务机器的具体实例，它不会直接去访问IP，它会去Eureka注册中心拿到服务的实例ID，即服务的名字。我再次使用客户端的负载均衡ribbon访问其中服务实例中的一台。

API网关主要为了服务本身对外的调用该怎么调用来解决的，还有解决权限校验的问题，你可以在这里整合调用一系列过滤器的，例如整合shiro,springsecurity之类的东西。
![9c88cf29a73969b788aaad34890fff7](http://520htt.com/upload/2020/05/9c88cf29a73969b788aaad34890fff7-d967e1e2d7e244dd94a57f193fa4c6e7.png)
## Zuul是什么
Zuul是一个L7应用程序网关，它提供了动态路由，监视，弹性，安全性等功能。请查看Wiki以获取用法，信息，HOWTO等
![95931eee79f190881bf51eb314aa4b9](http://520htt.com/upload/2020/05/95931eee79f190881bf51eb314aa4b9-6b3ad95f6af3424f85046273304b09a4.png)
Zuul可以通过加载动态过滤机制，从而实现以下各项功能：

　　1.验证与安全保障: 识别面向各类资源的验证要求并拒绝那些与要求不符的请求。

　　2.审查与监控: 在边缘位置追踪有意义数据及统计结果，从而为我们带来准确的生产状态结论。

　　3.动态路由: 以动态方式根据需要将请求路由至不同后端集群处。

　　4.压力测试: 逐渐增加指向集群的负载流量，从而计算性能水平。

　　5.负载分配: 为每一种负载类型分配对应容量，并弃用超出限定值的请求。

　　6.静态响应处理: 在边缘位置直接建立部分响应，从而避免其流入内部集群。

　　7.多区域弹性: 跨越AWS区域进行请求路由，旨在实现ELB使用多样化并保证边缘位置与使用者尽可能接近。
## 实战
pom依赖
```
  <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
        </dependency>
```
启动类加入`@EnableZuulProxy`注解
```
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class ZuulServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulServerApplication.class, args);
    }

}
```
配置文件
```
server:
  port: 8088
spring:
  application:
    name: zuul-server
eureka:
  instance:
    hostname: localhost
    prefer-ip-address: true #如果设置了eureka.instance.ip-address 属性，则使用该属性配置的IP
    instance-id: ${spring.application.name}:${server.port} #实例id
  client:
    service-url: # EurekaServer的地址，现在是自己的地址，如果是集群，需要加上其它Server的地址。
      defaultZone: http://${eureka.instance.hostname}:9093/eureka/
    registry-fetch-interval-seconds: 5 #从eureka服务器注册表中获取注册信息的时间间隔（s），默认为30秒
  ## zuul配置
zuul:
  retryable: true #重试机制
  prefix: /api #配置前缀
  routes:
    hystrix-order:
      path: /hystrix/**
      service-id: hystrix-order
```
启动`eureka-alone`,`hystrix-order`,` zuul-server`三个服务
通过`http://localhost:8088/api/hystrix/*`可以访问 hystrix-order微服务的任意接口。
## Zuul过滤器
#### ZuulFilter
ZuulFilter是过滤器的顶级父类。在这里我们看一下其中定义的4个最重要的方法：

```java
public abstract ZuulFilter implements IZuulFilter{

    abstract public String filterType();

    abstract public int filterOrder();
    
    boolean shouldFilter();// 来自IZuulFilter

    Object run() throws ZuulException;// IZuulFilter
}
```

- `shouldFilter`：返回一个`Boolean`值，判断该过滤器是否需要执行。返回true执行，返回false不执行。
- `run`：过滤器的具体业务逻辑。
- `filterType`：返回字符串，代表过滤器的类型。包含以下4种：
  - `pre`：请求在被路由之前执行
  - `routing`：在路由请求时调用
  - `post`：在routing和errror过滤器之后调用
  - `error`：处理请求时发生错误调用
- `filterOrder`：通过返回的int值来定义过滤器的执行顺序，数字越小优先级越高。


```

/**
 * @Author: jackgeeks
 * @ProjectName: springcloud-samples
 * @Package: com.wpp.zuulserver.filter
 * @ClassName: webFilter
 * @Description: @todo
 * @CreateDate: 2020/5/22 23:36
 * @Version: 1.0
 */
@Component
@Slf4j
public class webFilter extends ZuulFilter {
    @Override
    public String filterType() {
        // 登录校验，肯定是在前置拦截
        return "pre";
    }

    @Override
    public int filterOrder() {
        // 顺序设置为1
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        // 返回true，代表过滤器生效。
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        // 从上下文中获取request对象
        HttpServletRequest req = ctx.getRequest();
        log.info("HttpServletRequest对象"+req);


        // 校验通过，可以考虑把用户信息放入上下文，继续向后执行
        return null;
    }
}

```
## 获取更多关注公众号: 
![qrcode_for_gh_c8260435c2d7_258](http://520htt.com/upload/2020/05/qrcode_for_gh_c8260435c2d7_258-4703192829404b63b48d02e6e2cf9e1c.jpg)



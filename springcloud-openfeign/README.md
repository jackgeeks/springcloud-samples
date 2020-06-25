## Feign简介
Feign使用Jersey和CXF等工具为ReST或SOAP服务编写Java客户端。此外，Feign允许您在诸如Apache HC之类的http库之上编写自己的代码。Feign通过可自定义的解码器和错误处理功能，以最小的开销和代码将代码连接到http API，可以将其写入任何基于文本的http API。
>  Feign和OpenFeign的主要区别如下图
![8a6dc78160f41be32da6114e313ded7](http://520htt.com/upload/2020/05/8a6dc78160f41be32da6114e313ded7-77739ac3b93c4e9fa2c1215fc95cce13.png)

Feign基本上淘汰了

## 实战

pom依赖
```
  <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
```
启动类加上`@EnableFeignClients`注解
```
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class OpenfeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenfeignApplication.class, args);
    }
}

```
写一个接口通过`FeignClient("服务名")`指定微服务，通过方法进行调用
```
@FeignClient("payment")
public interface PaymentClient {
    @GetMapping("pay")
     String pay();

}
```
## 负载均衡
Feign中本身已经集成了Ribbon依赖和自动配置
因此我们不需要额外引入依赖，也不需要再注册`RestTemplate`对象。
可以通过`ribbon.xx`来进行全局配置。也可以通过`服务名.ribbon.xx`来对指定服务配置：
#### 配置重试
```
paymeny: #服务名
  ribbon:
    ConnectTimeout: 250 # Ribbon的连接超时时间
    ReadTimeout: 1000 # Ribbon的数据读取超时时间
    OkToRetryOnAllOperations: true # 是否对所有操作都进行重试
    MaxAutoRetriesNextServer: 1 # 切换实例的重试次数
    MaxAutoRetries: 1 # 对当前实例的重试次数
```

#### 修改负载均衡策略
新建策略类
```
@Configuration
public class myrule {
    @Bean
    public IRule my(){
        return  new RandomRule();//随机
    }
}

```
在FeignConfig通过`@RibbonClient(name = "服务名", configuration = 策略类.class)`

```
@Configuration
@RibbonClient(name = "payment", configuration = myrule.class)
public class FeignConfig {
}
```
**注意：不可以配置在PaymentClient接口上**


## 日志支持
编写配置类，定义日志级别
- NONE：不记录任何日志信息，这是默认值。
- BASIC：仅记录请求的方法，URL以及响应状态码和执行时间
- HEADERS：在BASIC的基础上，额外记录了请求和响应的头信息
- FULL：记录所有请求和响应的明细，包括头信息、请求体、元数据。


```
@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
```

通过`logging.level.xx=debug`来设置日志级别
```
## 日志
logging:
  level:
    com.wpp.openfeign.client.PaymentClient: debug
```

![b985636eef67036ac05339e4c9abb10](http://520htt.com/upload/2020/05/b985636eef67036ac05339e4c9abb10-3ed5adc7043d4c19a1da91981b82e1b0.png)
## 请求压缩
Spring Cloud Feign 支持对请求和响应进行GZIP压缩，以减少通信过程中的性能损耗。通过下面的参数即可开启请求与响应的压缩功能：
```
##  请求压缩
feign:
  compression:
    request:
      enabled: true # 开启请求压缩
      mime-types: text/html,application/xml,application/json # 设置压缩的数据类型
      min-request-size: 1024 #设置值触发压缩的大小下限,默认值：2048
    response:
      enabled: true # 开启响应压缩
```
![9cb851855496b486c3e4723627d7aed](http://520htt.com/upload/2020/05/9cb851855496b486c3e4723627d7aed-97b9265160314b2992a7d29c044e9eae.png)
SpringCloud-Gzip 相关文章：http://suo.im/67TKPx
## 获取更多关注公众号: 
![qrcode_for_gh_c8260435c2d7_258](http://520htt.com/upload/2020/05/qrcode_for_gh_c8260435c2d7_258-4703192829404b63b48d02e6e2cf9e1c.jpg)

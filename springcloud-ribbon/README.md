##  Ribbon简介：
Ribbon是一个基于HTTP和TCP的客户端负载均衡工具，它基于Netflix Ribbon实现。通过Spring Cloud的封装，可以让我们轻松地将面向服务的REST模版请求自动转换成客户端负载均衡的服务调用。Spring Cloud Ribbon虽然只是一个工具类框架，它不像服务注册中心、配置中心、API网关那样需要独立部署，但是它几乎存在于每一个Spring Cloud构建的微服务和基础设施中。因为微服务间的调用，API网关的请求转发等内容，实际上都是通过Ribbon来实现的，包括后续我们将要介绍的Feign，它也是基于Ribbon实现的工具。所以，对Spring Cloud Ribbon的理解和使用，对于我们使用Spring Cloud来构建微服务非常重要。
# Ribbon维护中
![543e117cfb025781f14296867292468](http://520htt.com/upload/2020/05/543e117cfb025781f14296867292468-9825157aabd94567b351530c60217dd5.png)
说明：因为Eureka2.0停止维护导致的
# 客户端负载均衡
负载均衡在系统架构中是一个非常重要，并且是不得不去实施的内容。因为负载均衡是对系统的高可用、网络压力的缓解和处理能力扩容的重要手段之一。我们通常所说的负载均衡都指的是服务端负载均衡，其中分为硬件负载均衡和软件负载均衡。硬件负载均衡主要通过在服务器节点之间按照专门用于负载均衡的设备，比如F5等；而软件负载均衡则是通过在服务器上安装一些用于负载均衡功能或模块等软件来完成请求分发工作，比如Nginx等。不论采用硬件负载均衡还是软件负载均衡，只要是服务端都能以类似下图的架构方式构建起来：
![d4862db8237b4100153df72d66e9022](http://520htt.com/upload/2020/05/d4862db8237b4100153df72d66e9022-7de5b8ed17ac4b379ef4f67f01f1c683.png)
  硬件负载均衡的设备或是软件负载均衡的软件模块都会维护一个下挂可用的服务端清单，通过心跳检测来剔除故障的服务端节点以保证清单中都是可以正常访问的服务端节点。当客户端发送请求到负载均衡设备的时候，该设备按某种算法（比如线性轮询、按权重负载、按流量负载等）从维护的可用服务端清单中取出一台服务端端地址，然后进行转发
# Ribbon负载均衡策略
![微信图片_20200516223105](http://520htt.com/upload/2020/05/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20200516223105-011bf09ee600492abf6afdec57a3c324.jpg)
1.RandomRule：
获取所有服务实例列表，循环通过一个随机数作为索引返回服务实例，直到返回一个不为空的服务实例。

2.RoundRobinRule：
采用线性轮询机制循环依次选择每个服务实例，直到选择到一个不为空的服务实例或循环次数达到10次。

3.RetryRule：
采用RoundRobinRule的选择机制，进行反复尝试，当花费时间超过设置的阈值maxRetryMills时，就返回null。

4.WeightedResponseTimeRule：
是RoundRobinRule的子类，对其进行了扩展，通过启动一个定时任务来为每个服务实例计算权重，然后遍历权重列表选择服务实例。

5.ClientConfigEnabledRoundRobinRule：
其内部实现了一个RoundRobinRule策略，我们可以继承它，在子类中做一些高级策略，当这些高级策略无法实施，可以用其默认实现的RoundRobinRule策略作为备选。

6.BestAvailableRule：
ClientConfigEnabledRoundRobinRule的子类，注入了负载均衡器的统计对象LoadBalancerStats，并利用它保存的实例统计信息来选择满足要求的实例。它会过滤掉故障的实例，并找出并发请求书最小的一个最空闲的实例。当LoadBalancerStats为空时无法执行，会利用ClientConfigEnabledRoundRobinRule的特性，采用默认实现的RoundRobinRule策略。

7.PredicateBasedRule：
ClientConfigEnabledRoundRobinRule的子类，基于Predicate实现的策略。通AbstractServerPredicate的chooseRoundRobinAfterFiltering方法进行过滤，获取备选的服务实例清单，然后用线性轮询选择一个实例，是一个抽象类，过滤策略在AbstractServerPredicate的子类中具体实现。

8.AvailabilityFilteringRule：
PredicateBasedRule的子类，原理与PredicateBasedRule相同，过滤规则：实例是否故障，即断路器是否生效，已将实例断开；实例的并发请求数是否大于阈值。只要有一个满足，就将这个实例过滤掉。其与父类的差别是：先以线性的方式选择一个实例，然后用过滤规则过滤，如果没被过滤掉则直接使用该实例，否则选取下一个实例继续进行过滤。如果重复十次还未选择到实例则采取父类的策略。

9.ZoneAvoidanceRule：
PredicateBasedRule的子类，原理与PredicateBasedRule相同，过滤规则：以ZoneAvoidancePredicate为主条件，AvailabilityFilteringPredicate为次条件组合进行过滤。使用主过滤条件过滤所有实例并返回清单，依次使用次过滤条件对清单进行过滤，每次过滤后判断两个条件，两个都符合就不再过滤：过滤后的实例总数 >= 最小过滤实例数（默认为1）；过滤后的实例比例 > 最小过滤百分比（默认为0）。)

其中IRule为负载均衡策略的总接口，声明了choose、setLoadBalancer、getLoadBalancer。AbstractLoadBalancerRule继承IRule，为负载均衡策略的抽象类，定义了ILoadBalancer负载均衡器，实现了IRule接口中的setLoadBalancer和getLoadBalancer方法，即ILoadBalancer的get，set方法，上边9种负载均衡策略都是其子类，在其基础上进行扩展。

# 工作原理
![e49ba72d0e913c9497fabc22be4d54e](http://520htt.com/upload/2020/05/e49ba72d0e913c9497fabc22be4d54e-c53c014790fa47259e7d74b5d1cccd6d.png)
1.获取@LoadBalanced注解标记的RestTemplate。
2.RestTemplate添加一个拦截器(filter)，当使用RestTemplate发起http调用时进行拦截。
3.在filter拦截到该请求时，获取该次请求服务集群的全部列表信息。
4.根据规则从集群中选取一个服务作为此次请求访问的目标。
5.访问该目标，并获取返回结果。

# 实战
##### 1. pom说明：
spring-cloud-starter-netflix-eureka-client2.2.0的依赖自带Ribbon依赖
![4d5e929021860c5bb61bdb952f69e32](http://520htt.com/upload/2020/05/4d5e929021860c5bb61bdb952f69e32-8faf4d37c6cb4d5d9155e6ff26246649.png)
##### 2.新建两个paymeny服务提供/pay接口注入eureka中（代码略）
##### 3.新建ribbon服务注入eureka中
配置文件
```
server:
  port: 8084
spring:
  application:
    name: ribbon
  security:
    user:
      name: "user"
      password: "admin"
eureka:
  instance:
    hostname: localhost
    prefer-ip-address: true #如果设置了eureka.instance.ip-address 属性，则使用该属性配置的IP
    instance-id: ${spring.application.name}:${server.port} #实例id
  client:
    service-url: # EurekaServer的地址，现在是自己的地址，如果是集群，需要加上其它Server的地址。
      defaultZone: http://${spring.security.user.name}:${spring.security.user.password}@${eureka.instance.hostname}:9090/eureka/,http://${spring.security.user.name}:${spring.security.user.password}@${eureka.instance.hostname}:9091/eureka/
    registry-fetch-interval-seconds: 5 #从eureka服务器注册表中获取注册信息的时间间隔（s），默认为30秒
```
启动类添加`@EnableDiscoveryClient`注解
注入restTemplate加上 `@LoadBalanced`注解
```
@SpringBootApplication
@EnableDiscoveryClient
public class RibbonApplication {

    public static void main(String[] args) {
        SpringApplication.run(RibbonApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 因为加入Security这段配置让Security失效
     *
     *
     */
    @Configuration
    public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().permitAll()
                    .and().csrf().disable();
        }
    }


}

```

使用restTemplate调用paymeny微服务
```
@RestController
public class RibbonController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("pay")
    public ResponseEntity<String> pay(){
        ResponseEntity<String> entity = restTemplate.getForEntity("http://payment/pay", String.class);

        return  entity;
    }

}
```
访问`http://localhost:8084/pay`观察负载均衡（默认是轮询）

## 修改负载均衡策略
新建一个配置类，但是不可以启动类扫描到 如图：
![97c122c64295ed7e5ff1028d2760e62](http://520htt.com/upload/2020/05/97c122c64295ed7e5ff1028d2760e62-7ae09ae3545a460a925a9b95fb496279.png)

```
@Configuration
public class myrule {
    @Bean
    public IRule my(){
        return  new RandomRule();//随机
    }
}

```
在RibbonController类加上`@RibbonClient(name = "服务名", configuration = 配置类)`
![5131581b424761eb3b396762d0b062e](http://520htt.com/upload/2020/05/5131581b424761eb3b396762d0b062e-f881876f5b8f49b18bb613e0ee26b894.png)
### 重试机制
Eureka的服务治理强调了[CAP](https://baike.baidu.com/item/CAP%E5%8E%9F%E5%88%99/5712863?fr=aladdin)原则中的AP，即可用性和可靠性。它与Zookeeper这一类强调CP（一致性，可靠性）的服务治理框架最大的区别在于：Eureka为了实现更高的服务可用性，牺牲了一定的一致性，极端情况下它宁愿接收故障实例也不愿丢掉健康实例，正如我们上面所说的自我保护机制。
但是，此时如果我们调用了这些不正常的服务，调用就会失败，从而导致其它服务不能正常工作！这显然不是我们愿意看到的。
因此Spring Cloud 整合了Spring Retry 来增强RestTemplate的重试能力，当一次服务调用失败后，不会立即抛出一次，而是再次重试另一个服务。


```
 <dependency>
            <groupId>org.springframework.retry</groupId>
            <artifactId>spring-retry</artifactId>
        </dependency>
```

在修改配置文件加入配置：
```
spring:
  cloud:
    loadbalancer:
      retry:
        enabled: true # 开启Spring Cloud的重试功能
paymeny: #服务名
  ribbon:
    ConnectTimeout: 250 # Ribbon的连接超时时间
    ReadTimeout: 1000 # Ribbon的数据读取超时时间
    OkToRetryOnAllOperations: true # 是否对所有操作都进行重试
    MaxAutoRetriesNextServer: 1 # 切换实例的重试次数
    MaxAutoRetries: 1 # 对当前实例的重试次数
```
## 获取更多关注公众号: 
![qrcode_for_gh_c8260435c2d7_258](http://520htt.com/upload/2020/05/qrcode_for_gh_c8260435c2d7_258-4703192829404b63b48d02e6e2cf9e1c.jpg)



# 什么是服务注册中心
服务注册中心是服务实现服务化管理的核心组件,类似于目录服务的作用,主要用来存储服务信息，譬如提供者url串、路由信息等。服务注册中心是SOA架构中最基础的设施之一。
# 服务注册中心的作用
* 服务注册
* 服务发现
# 服务注册中心有那些
Zookeeper，Eureka，Consul，ETCD，Nacos  ....
# 服务注册中心对比：
![20190916151417612](http://520htt.com/upload/2020/05/20190916151417612-88763c7bc12f4569b130e37a6a3080db.png)
# Eureka简介：
Netflix是一家在线影片租赁提供商。

Spring Cloud Eureka是Spring Cloud Netflix项目下的服务治理模块。而Spring Cloud Netflix项目是Spring Cloud的子项目之一，主要内容是对Netflix公司一系列开源产品的包装，它为Spring Boot应用提供了自配置的Netflix OSS整合。通过一些简单的注解，开发者就可以快速的在应用中配置一下常用模块并构建庞大的分布式系统。它主要提供的模块包括：服务发现（Eureka），断路器（Hystrix），智能路由（Zuul），客户端负载均衡（Ribbon）等。

Eureka是基于REST（代表性状态转移）的服务，主要在AWS云中用于定位服务，以实现负载均衡和中间层服务器的故障转移。
在Netflix，Eureka除了在中间层负载平衡中发挥关键作用外，还用于以下目的。

* 为了帮助Netflix Asgard-一种开源服务，可简化云部署
    1.在出现问题的情况下快速回滚版本，以避免重新启动100个实例，这可能需要很长时间。
    2.在滚动推送中，以避免出现问题时将新版本传播到所有实例。

* 对于我们的cassandra部署，可以使实例流量减少以进行维护。

* 为我们的memcached缓存服务标识环中的节点列表。

* 用于出于各种其他原因承载有关服务的其他其他特定于应用程序的元数据。

# Eureka服务发现原理
![6ba84e8b181c65a7852ddfa2492f252](http://520htt.com/upload/2020/05/6ba84e8b181c65a7852ddfa2492f252-279d171912e54a88b051763df59fa8c6.png)
服务提供者、服务消费者、服务发现组件这三者之间的关系大致如下：

* 各个微服务在启动时，将自己的网络地址等信息注册到服务发现组件中，服务发现组件会存储这些信息；

* 服务消费者可从服务发现组件查询服务提供者的网络地址，并使用该地址调用服务提供者的接口；

* 各个微服务与服务发现组件使用一定机制（例如心跳）通信。服务发现组件如长时间无法与某微服务实例通信，就会自动注销（即：删除）该实例；

* 当微服务网络地址发生变更（例如实例增减或者IP端口发生变化等）时，会重新注册到服务发现组件；

* 客户端缓存：各个微服务将需要调用服务的地址缓存在本地，并使用一定机制更新（例如定时任务更新、事件推送更新等）。这样既能降低服务发现组件的压力，同时，即使服务发现组件出问题，也不会影响到服务之间的调用。

综上，服务发现组件应具备以下功能。

* 服务注册表：服务注册表是服务发现组件的核心（其实就是类似于上面的registry表），它用来记录各个微服务的信息，例如微服务的名称、IP、端口等。服务注册表提供查询API和管理API，查询API用于查询可用的微服务实例，管理API用于服务的注册和注销；

* 服务注册与服务发现：服务注册是指微服务在启动时，将自己的信息注册到服务发现组件上的过程。服务发现是指查询可用微服务列表及其网络地址的机制；

* 服务检查：服务发现组件使用一定机制定时检测已注册的服务，如发现某实例长时间无法访问，就会从服务注册表中移除该实例。
# Eureka 宣布闭源
```
The existing open source work on eureka 2.0 is discontinued. The code base and artifacts that were released as part of the existing repository of work on the 2.x branch is considered use at your own risk.

Eureka 1.x is a core part of Netflix's service discovery system and is still an active project.
```
来自：https://github.com/Netflix/eureka/wiki
看清楚官方的言辞：官方只是说Eureka 2.0的开发被停止了，如果您将Eureka 2.0分支用在生产，将后果自负！

对于 Eureka 的闭源，Spring Cloud 将何去何从？后续会不会替换默认的服务注册组件呢？不得而知，Spring Cloud 版本发布很快，已经快跟不上了。

Eureka 2.x 还未发布正式版本，Spring Cloud 还是在 1.x 上面开发的，最新版本依赖 1.9.2，所以虽然国内大多数公司也在用 Eureka，但暂时不会受影响。



1.x 相对稳定，建议不要盲目升级或者切换到别的中间件。不过，随着 Eureka 的闭源，后续还是有必要迁移至 Consul、ZooKeeper、Etcd 等开源中间件上面去的。

如果是公司近期开发新项目建议使用 [Spring Cloud Alibaba](https://spring.io/projects/spring-cloud-alibaba)

# 实战
### eureka-server
### pom依赖
```
 <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>

```
###加`@EnableEurekaServer`注解
```
@SpringBootApplication
@EnableEurekaServer
public class EurekaServer01Application {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServer01Application.class, args);
    }

}
```

### 单机版
配置文件
```
server:
  port: 9090
spring:
  application:
    name: eureka-server
eureka:
  instance:
    hostname: localhost
    prefer-ip-address: true #如果设置了eureka.instance.ip-address 属性，则使用该属性配置的IP
  client:
    service-url: # EurekaServer的地址，现在是自己的地址，如果是集群，需要加上其它Server的地址。
      defaultZone: http://${eureka.instance.hostname}:9090/eureka/
    register-with-eureka: false # 是否注册自己的信息到EurekaServer，默认是true
    fetch-registry: false # 是否拉取其它服务的信息，默认是true
```
启动
浏览器输入`http://localhost:9090/`
![2eff0999d17d1cf82c8d5194f085a20](http://520htt.com/upload/2020/05/2eff0999d17d1cf82c8d5194f085a20-4e5c3d958bb0456688da4231735d9c20.png)

### 集群版
Eureka Server即服务的注册中心，在刚才的案例中，我们只有一个EurekaServer，事实上EurekaServer也可以是一个集群，形成高可用的Eureka中心。
###服务同步
多个Eureka Server之间也会互相注册为服务，当服务提供者注册到Eureka Server集群中的某个节点时，该节点会把服务的信息同步给集群中的每个节点，从而实现**数据同步**。因此，无论客户端访问到Eureka Server集群中的任意一个节点，都可以获取到完整的服务列表信息。
#### 修改原来的EurekaServer配置
```
server:
  port: 9090
spring:
  application:
    name: eureka-server
eureka:
  instance:
    hostname: localhost
    prefer-ip-address: true #如果设置了eureka.instance.ip-address 属性，则使用该属性配置的IP
  client:
    service-url: # EurekaServer的地址，现在是自己的地址，如果是集群，需要加上其它Server的地址。
      defaultZone: http://${eureka.instance.hostname}:9090/eureka/,http://${eureka.instance.hostname}:9091/eureka/
```
所谓的高可用注册中心，其实就是把EurekaServer自己也作为一个服务进行注册，这样多个EurekaServer之间就能互相发现对方，从而形成集群。因此我们做了以下修改：

- 删除了register-with-eureka=false和fetch-registry=false两个配置。因为默认值是true，这样就会吧自己注册到注册中心了。
- 把service-url的值改成了另外一台EurekaServer的地址，而不是自己

#### 另外一台配置恰好相反：
```
server:
  port: 9090
spring:
  application:
    name: eureka-server
eureka:
  instance:
    hostname: localhost
    prefer-ip-address: true #如果设置了eureka.instance.ip-address 属性，则使用该属性配置的IP
  client:
    service-url: # EurekaServer的地址，现在是自己的地址，如果是集群，需要加上其它Server的地址。
      defaultZone: http://${eureka.instance.hostname}:9090/eureka/,http://${eureka.instance.hostname}:9091/eureka/
```
启动
浏览器输入`http://localhost:9090/`或者`http://localhost:9091/`
![3f3c83e1821e600a187f2d1901b5703](http://520htt.com/upload/2020/05/3f3c83e1821e600a187f2d1901b5703-e52117e8585441c5a23d97c445bbbcb6.png)
## consumer
pom依赖
```
 <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
```
添加`@EnableEurekaClient`注解
```
@SpringBootApplication
@EnableEurekaClient
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}

```
配置文件
```
server:
  port: 8081
spring:
  application:
    name: consumer
eureka:
  instance:
    hostname: localhost
    prefer-ip-address: true #如果设置了eureka.instance.ip-address 属性，则使用该属性配置的IP
    instance-id: ${spring.application.name}:${server.port} #实例id
  client:
    service-url: # EurekaServer的地址，现在是自己的地址，如果是集群，需要加上其它Server的地址。
      defaultZone: http://${eureka.instance.hostname}:9090/eureka/,http://${eureka.instance.hostname}:9091/eureka/
```
##重要参数解释：
## 服务提供者
服务提供者要向EurekaServer注册服务，并且完成服务续约等工作。
> 服务注册

服务提供者在启动时，会检测配置属性中的：`eureka.client.register-with-erueka=true`参数是否正确，事实上默认就是true。如果值确实为true，则会向EurekaServer发起一个Rest请求，并携带自己的元数据信息，Eureka Server会把这些信息保存到一个双层Map结构中。第一层Map的Key就是服务名称，第二层Map的key是服务的实例id。

> 服务续约

在注册服务完成以后，服务提供者会维持一个心跳（定时向EurekaServer发起Rest请求），告诉EurekaServer：“我还活着”。这个我们称为服务的续约（renew）；

有两个重要参数可以修改服务续约的行为：

```yaml
eureka:
  instance:
    lease-expiration-duration-in-seconds: 90
    lease-renewal-interval-in-seconds: 30
```

- lease-renewal-interval-in-seconds：服务续约(renew)的间隔，默认为30秒
- lease-expiration-duration-in-seconds：服务失效时间，默认值90秒

也就是说，默认情况下每个30秒服务会向注册中心发送一次心跳，证明自己还活着。如果超过90秒没有发送心跳，EurekaServer就会认为该服务宕机，会从服务列表中移除，这两个值在生产环境不要修改，默认即可。

但是在开发时，这个值有点太长了，经常我们关掉一个服务，会发现Eureka依然认为服务在活着。所以我们在开发阶段可以适当调小。

```yaml
eureka:
  instance:
    lease-expiration-duration-in-seconds: 10 # 10秒即过期
    lease-renewal-interval-in-seconds: 5 # 5秒一次心跳
```
> 实例id

在status一列中，显示以下信息：

- UP(1)：代表现在是启动了1个示例，没有集群
- DESKTOP-2MVEC12:user-service:9090：是示例的名称（instance-id），
  - 默认格式是：`${hostname} + ${spring.application.name} + ${server.port}`
  - instance-id是区分同一服务的不同实例的唯一标准，因此不能重复。

我们可以通过instance-id属性来修改它的构成：

```yaml
eureka:
  instance:
    instance-id: ${spring.application.name}:${server.port}
```

重启服务再试试看：
![630643cffc1e258e981b8c0e36b99a3](http://520htt.com/upload/2020/05/630643cffc1e258e981b8c0e36b99a3-e90957af6f7047d78326d1707ca85484.png)
## 服务消费者
> 获取服务列表

当服务消费者启动是，会检测`eureka.client.fetch-registry=true`参数的值，如果为true，则会从Eureka Server服务的列表只读备份，然后缓存在本地。并且`每隔30秒`会重新获取并更新数据。我们可以通过下面的参数来修改：

```yaml
eureka:
  client:
    registry-fetch-interval-seconds: 5
```

生产环境中，我们不需要修改这个值。

但是为了开发环境下，能够快速得到服务的最新状态，我们可以将其设置小一点。
### 失效剔除和自我保护

> 失效剔除

有些时候，我们的服务提供方并不一定会正常下线，可能因为内存溢出、网络故障等原因导致服务无法正常工作。Eureka Server需要将这样的服务剔除出服务列表。因此它会开启一个定时任务，每隔60秒对所有失效的服务（超过90秒未响应）进行剔除。

可以通过`eureka.server.eviction-interval-timer-in-ms`参数对其进行修改，单位是毫秒，生成环境不要修改。

这个会对我们开发带来极大的不变，你对服务重启，隔了60秒Eureka才反应过来。开发阶段可以适当调整，比如10S

> 自我保护

我们关停一个服务，就会在Eureka面板看到一条警告：

![4735cf30dcd7fca3bba76b070b406c5](http://520htt.com/upload/2020/05/4735cf30dcd7fca3bba76b070b406c5-93ef5b096df84be397828d2844912035.png)

这是触发了Eureka的自我保护机制。当一个服务未按时进行心跳续约时，Eureka会统计最近15分钟心跳失败的服务实例的比例是否超过了85%。在生产环境下，因为网络延迟等原因，心跳失败实例的比例很有可能超标，但是此时就把服务剔除列表并不妥当，因为服务可能没有宕机。Eureka就会把当前实例的注册信息保护起来，不予剔除。生产环境下这很有效，保证了大多数服务依然可用。

但是这给我们的开发带来了麻烦， 因此开发阶段我们都会关闭自我保护模式：

```yaml
eureka:
  server:
    enable-self-preservation: false # 关闭自我保护模式（缺省为打开）
    eviction-interval-timer-in-ms: 1000 # 扫描失效服务的间隔时间（缺省为60*1000ms）
```
### 开发时建议配置
```
server:
  port: 9090
spring:
  application:
    name: eureka-server
eureka:
  instance:
    hostname: localhost
    prefer-ip-address: true #如果设置了eureka.instance.ip-address 属性，则使用该属性配置的IP
    lease-expiration-duration-in-seconds: 10 # 10秒即过期
    lease-renewal-interval-in-seconds: 5 # 5秒一次心跳
    instance-id: ${spring.application.name}:${server.port} #实例id
  client:
    service-url: # EurekaServer的地址，现在是自己的地址，如果是集群，需要加上其它Server的地址。
      defaultZone: http://${eureka.instance.hostname}:9090/eureka/,http://${eureka.instance.hostname}:9091/eureka/
  server:
    enable-self-preservation: false # 关闭自我保护模式（缺省为打开）
    eviction-interval-timer-in-ms: 1000 # 扫描失效服务的间隔时间（缺省为60*1000ms）
```
### 生产建议配置
```
server:
  port: 9091
spring:
  application:
    name: eureka-server
eureka:
  instance:
    hostname: localhost
    prefer-ip-address: true #如果设置了eureka.instance.ip-address 属性，则使用该属性配置的IP
    instance-id: ${spring.application.name}:${server.port} #实例id
  client:
    service-url: # EurekaServer的地址，现在是自己的地址，如果是集群，需要加上其它Server的地址。
      defaultZone: http://${eureka.instance.hostname}:9090/eureka/,http://${eureka.instance.hostname}:9091/eureka/
```
# 关于Eureka安全

Eureka本身不具备安全认证的能力，Spring Cloud使用Spring Security为Eureka Server进行了增强。

添加spring security依赖
```
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```
编写SecurityConfig配置 
```
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/eureka/**");
        super.configure(http);
    }
}
```
配置文件
将Eureka Server中的 eureka.client.service-url.defaultZone 修改为为http://{user}:{password}@ip/eureka/ 的形式：
```
server:
  port: 9091
spring:
  application:
    name: eureka-server
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
```
## 获取源码公众号回复: Eureka
![qrcode_for_gh_c8260435c2d7_258](http://520htt.com/upload/2020/05/qrcode_for_gh_c8260435c2d7_258-4703192829404b63b48d02e6e2cf9e1c.jpg)






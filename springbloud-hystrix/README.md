## 为什么需要Hystrix
![微信图片_20200521013243](http://520htt.com/upload/2020/05/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20200521013243-9b894be981ac439f812acf36904fd0f7.png)
在高并发访问下，这些依赖的稳定性与否对系统的影响非常大，但是依赖有很多不可控问题：如网络连接缓慢，资源繁忙，暂时不可用，服务脱机等，如下图：
![db06810d324278a078aa47bf396761a](http://520htt.com/upload/2020/05/db06810d324278a078aa47bf396761a-16347483ebd2451e8f6e8c220ee18291.png)
当依赖阻塞时，大多数服务器的线程池就出现阻塞，影响整个线上服务的稳定性，如下图：
![650c9db62cc0aa7e486a9655d41924a](http://520htt.com/upload/2020/05/650c9db62cc0aa7e486a9655d41924a-4b9b5a8e81b2461098b3a62dffae387b.png)

##  什么是Hystrix？
在分布式环境中，不可避免地会有许多服务依赖项中的某些失败。Hystrix是一个库，可通过添加等待时间容限和容错逻辑来帮助您控制这些分布式服务之间的交互。Hystrix通过隔离服务之间的访问点，停止服务之间的级联故障并提供后备选项来实现此目的，所有这些都可以提高系统的整体弹性。
#### Hystrix的历史
Hystrix源自Netflix API团队于2011年开始的弹性工程工作。2012年，Hystrix不断发展和成熟，Netflix内部的许多团队都采用了它。如今，每天在Netflix上通过Hystrix执行数百亿个线程隔离和数千亿个信号量隔离的调用。这极大地提高了正常运行时间和弹性。
## Hystrix解决什么问题？
复杂的分布式体系结构中的应用程序具有数十个依赖项，每个依赖项都会不可避免地在某个时刻失败。如果主机应用程序未与这些外部故障隔离开来，则可能会被淘汰。

>例如，对于依赖于30个服务的应用程序，其中每个服务的正常运行时间为99.99％，您可以期望：
99.99 30 = 99.7％的正常运行时间 10亿个请求中的0.3％= 3,000,000个故障/每月2小时以上的停机时间，即使所有依赖项都具有出色的正常运行时间。

现实通常更糟。

即使您没有对整个系统进行永续性设计，即使所有依赖项都能很好地执行，即使宕机0.01％，对数十种服务中的每一项的总影响也等于一个月可能会有数小时的宕机。
## Hystrix的设计原则是什么？
Hystrix的工作原理：
防止任何单个依赖项耗尽所有容器（例如Tomcat）用户线程。
减少负载并快速失败，而不是排队。
在可行的情况下提供备用，以保护用户免受故障的影响。
使用隔离技术（例如隔板，泳道和断路器模式）来限制任何一种依赖关系的影响。
通过近实时指标，监视和警报优化发现时间
通过在Hystrix的大多数方面中以低延迟传播配置更改来优化恢复时间，并支持动态属性更改，这使您可以通过低延迟反馈回路进行实时操作修改。
防止整个依赖性客户端执行失败，而不仅仅是网络流量失败。
## Hystrix的作用是什么？
* 降级
* 熔断
* 限流
* 监控
## Hystrix状态
Hystrix不再处于主动开发中，并且当前处于维护模式。

Hystrix（1.5.18版）足够稳定，可以满足Netflix现有应用程序的需求。同时，我们的重点已转向对应用程序的实时性能做出反应的自适应性实现，而不是预先配置的设置（例如，通过自适应并发限制）。对于像Hystrix这样有意义的情况，我们打算继续将Hystrix用于现有应用程序，并为新的内部项目利用诸如resilience4j之类的开放式活动项目。我们开始建议其他人也这样做。

Netflix Hystrix现在正式处于维护模式，对整个社区具有以下期望：Netflix将不再主动审查问题，合并请求并发布新版本的Hystrix。我们已经根据1891年的最终版本发布了Hystrix（1.5.18），以便Maven Central中的最新版本与Netflix内部使用的最新已知稳定版本（1.5.11）保持一致。如果社区成员有兴趣获得Hystrix的所有权并将其移回活动模式，请联系hystrixoss@googlegroups.com。

多年来，Hystrix为Netflix和社区提供了良好的服务，向维护模式的过渡绝不表示Hystrix的概念和想法不再有价值。相反，Hystrix启发了许多伟大的想法和项目。我们感谢Netflix和整个社区中的每个人多年来为Hystrix所做的所有贡献。


## 实战
项目说明：
>hystrix-order 服务提供方
hystrix-feign 服务消费方

 依赖
```
  <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
```
启动类加入`@EnableHystrix`注解 
```
@SpringBootApplication
@EnableHystrix
@EnableDiscoveryClient
public class HystrixOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixOrderApplication.class, args);
    }

}

```
配置文件（略）
###  降级
hystrix-order局部配置`@HystrixCommand(fallbackMethod = "降级方法名"）`
```
RestController
@Slf4j
public class LocalHystrixController {


    @GetMapping("/timeout/{id}")
    @HystrixCommand(fallbackMethod = "FallBackMethod",commandProperties = {
            //设置这个线程的超时时间是3s，3s内是正常的业务逻辑，超过3s调用fallbackMethod指定的方法进行处理
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
    })
    public String LocalHystrixTimeOut( @PathVariable("id") Integer id){
        int timeNumber = 1;
        try{
            TimeUnit.SECONDS.sleep(timeNumber);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return "线程池："+Thread.currentThread().getName()+id+"\t"+"O(∩_∩)O哈哈~"+"   耗时(秒)："+timeNumber;
    }


    public String FallBackMethod(@PathVariable("id") Integer id){
        return "请稍后再试，o(╥﹏╥)o";
    }


}
```
>  LocalHystrixTimeOut通过`timeNumber`变量对线程时间进行控制，通过`@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")` 配置如果超过三秒响应进行服务降级


hystrix-order全局配置通过`@DefaultProperties(defaultFallback = "降级方法")`，并在方法上加入`@HystrixCommand`
```
@RestController
@DefaultProperties(defaultFallback = "GlobalFallbackMethod")
public class GlobalHystrixController {
    @GetMapping("/globa1")
    @HystrixCommand(commandProperties = {
            //设置这个线程的超时时间是3s，3s内是正常的业务逻辑，超过3s调用fallbackMethod指定的方法进行处理
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
    })
    public String globa1(){
        int timeNumber = 10;
        try{
            TimeUnit.SECONDS.sleep(timeNumber);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return "线程池："+Thread.currentThread().getName()+"\t"+"O(∩_∩)O哈哈~"+"   耗时(秒)："+timeNumber;
    }
    @GetMapping("/globa2")
    @HystrixCommand
    public String globa2(){
         int  i=10/0;

        return "O(∩_∩)O哈哈~";
    }

    public String GlobalFallbackMethod(){
        return "Global异常处理信息，请稍后再试。/(╥﹏╥)/~~";
    }

}

```
### 对feign 的支持
开启Feign的熔断功能
```
feign:
  hystrix:
    enabled: true # 开启Feign的熔断功能
```
通过`@FeignClient(value = "服务名" ,fallback = 降级类.class)`
```
@FeignClient(value = "hystrix-order" ,fallback = HystrixApiImpl.class)
public interface HystrixApi {

    @GetMapping("/port")
    String GetPort();
    @GetMapping("/time")
    String GetTime();
}
```
降级类
```
@Component
public class HystrixApiImpl implements HystrixApi {
    @Override
    public String GetPort() {
        return "接口异常，请稍后再试，o(╥﹏╥)o";
    }

    @Override
    public String GetTime() {
        return "接口异常，请稍后再试，o(╥﹏╥)o";
    }
}

```
## 熔断
####  熔断器:Circuit Breaker
Circuit Breaker 流程架构和统计:
![3473cdd92fe747fdf82e607cad65642](http://520htt.com/upload/2020/05/3473cdd92fe747fdf82e607cad65642-bd72d62210a143689d9b1d89ab3ed9d7.png)
#### 熔断器理论
关于熔断器的大神论文：http://suo.im/5SjSGe
![微信图片_20200521021551](http://520htt.com/upload/2020/05/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20200521021551-be0e84dbc5c44e1ba64ec2fa8adb7d18.png)

>该示例是一个简单的说明性示例，实际上断路器提供了更多的功能和参数设置。通常，它们可以防止受保护呼叫可能引发的一系列错误，例如网络连接失败。并非所有错误都应使电路跳闸，某些错误应反映正常故障并作为常规逻辑的一部分进行处理。

>由于通信量大，您可能在等待初始超时时遇到许多呼叫问题。由于远程调用通常很慢，因此最好将每个调用放在一个不同的线程上，以将来使用或承诺在返回时处理结果。通过从线程池中提取这些线程，可以安排在线程池用尽时电路中断。

>该示例显示了一种使断路器跳闸的简单方法-在成功呼叫后重置计数。一种更复杂的方法可能是查看错误发生的频率，一旦出现故障率达到50％，就会跳闸。对于不同的错误，您可能还具有不同的阈值，例如对于超时，阈值为10，对于连接失败，阈值为3。

>我展示的示例是用于同步调用的断路器，但是断路器对于异步通信也很有用。此处的一种常用技术是将所有请求都放在一个队列中，供方以它的速度消费该队列-一种有用的技术，可以避免服务器超负荷。在这种情况下，队列已满时电路就会中断。

>断路器本身有助于减少可能会失败的操作所占用的资源。您可以避免等待客户端的超时，并且电路断路可以避免负载增加服务器的负担。我在这里谈论的是远程呼叫，这是断路器的一种常见情况，但是它们可以用于任何需要保护系统部件免受其他部件故障影响的情况。

>断路器是进行监视的宝贵场所。断路器状态的任何更改都应记录下来，断路器应显示其状态的详细信息以进行更深入的监视。断路器行为通常是警告有关环境中更深层故障的好来源。操作人员应能够跳闸或重置断路器。

>断路器本身很有价值，但是使用断路器的客户需要对断路器故障做出反应。与任何远程调用一样，您需要考虑发生故障时的处理方法。它是否会使您正在执行的操作失败，或者您可以采取解决方法？可以将信用卡授权放到队列中以备后用，可以通过显示一些足以显示的陈旧数据来减轻无法获取某些数据的麻烦。

CircuitBreakerService 熔断类
```
@Service
public class CircuitBreakerService {
    /**
     *  失败率达到一定值后跳闸,即使输入正确的URL也不可以访问，
     * 当失败率下降，恢复服务
     * @param id
     * @return
     */
    //服务熔断
    @HystrixCommand(fallbackMethod = "circuitBreaker_fallback",commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),   //是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),  //请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),    //时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"),    //失败率达到多少后跳闸
    })
    public String CircuitBreaker(@PathVariable("id") Integer id){
        if(id < 0){
            throw new RuntimeException("******id 不能为负数");
        }
        String serialNumber = IdUtil.simpleUUID();

        return Thread.currentThread().getName()+"\t"+"调用成功，流水号："+serialNumber;
    }
    public String circuitBreaker_fallback(@PathVariable("id") Integer id){
        return "id 不能负数，请稍后再试，o(╥﹏╥)o  id："+id;
    }
}
```
服务调用
```
@RestController
public class CircuitBreakerController {
    @Autowired
    private CircuitBreakerService CircuitBreaker;


    @GetMapping("/fuse/{id}")
    public ResponseEntity<String> Fuse(@PathVariable ("id") Integer id){
        return  ResponseEntity.ok(CircuitBreaker.CircuitBreaker(id));
    }


}
```
错误URL：http://localhost:8086/fuse/-10
正确URL：http://localhost:8086/fuse/10
> 输入错误的URL失败率达到一定值后跳闸,即使输入正确的URL也不可以访问， 当失败率下降，恢复服务
## 限流

#### 为什么需要限流
* 复杂分布式系统通常有很多依赖，如果一个应用不能对来自依赖 故障进行隔离，那么应用本身就处在被拖垮的风险中。在一个高流量的网站中，某个单一后端一旦发生延迟，将会在数秒内导致 所有应用资源被耗尽（一个臭鸡蛋影响一篮筐）。
* 如秒杀、抢购、双十一等场景，在某一时间点会有爆发式的网络流量涌入，如果没有好的网络流量限制，任由流量压到后台服务实例，很有可能造成资源耗尽，服务无法响应，甚至严重的导致应用崩溃。

通过`@HystrixProperty(name = "coreSize", value = "5")`限制流量
```
@RestController
public class LimitingComtroller {
    //对controller层的接口做hystrix线程池隔离，可以起到限流的作用
    @HystrixCommand(
            fallbackMethod = "fallbackMethod",//指定降级方法，在熔断和异常时会走降级方法
            commandProperties = {
                    //超时时间
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
            },
            threadPoolProperties = {
                    //并发，缺省为10
                    @HystrixProperty(name = "coreSize", value = "5")
            }
    )
    @GetMapping(value = "/Limiting")
    public String sayHello(HttpServletResponse httpServletResponse){
        return "Hello World";
    }

    /**
     *  降级方法，状态码返回503
     *  注意，降级方法的返回类型与形参要与原方法相同，可以多一个Throwable参数放到最后，用来获取异常信息
     */
    public String fallbackMethod(HttpServletResponse httpServletResponse,Throwable e){
        httpServletResponse.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        return e.getMessage();
    }
}

```
通过jmeter发送10个并发请求，如图5个成功，5个失败
![0baeb3141f89b274069d2ec01f9bfcd](http://520htt.com/upload/2020/05/0baeb3141f89b274069d2ec01f9bfcd-67ca28a5ba1144a486fd616e01d2a84f.png)

关于hystrix的超时时间的配置
```
##Ribbon的超时时间一定要小于Hystix的超时时间。
##hystrix的超时时间
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMillisecond: 6000 # 设置hystrix的超时时间
```
## 监控
依赖
```
   <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
        </dependency>
```
添加`@EnableHystrixDashboard`
```
@SpringBootApplication
@EnableHystrixDashboard
public class HystrixDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardApplication.class, args);
    }

}
```
配置文件（略）
`http://localhost:9004/hystrix`访问
![微信图片_20200521025312](http://520htt.com/upload/2020/05/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20200521025312-74b80b413d0b4bf2b8afe7afd450cadb.png)

☞默认的集群监控：通过URL:http://turbine-hostname:port/turbine.stream开启，实现对默认集群的监控。

☞指定的集群监控：通过URL:http://turbine-hostname:port/turbine.stream?cluster=[clusterName]开启，实现对clusterName集群的监控。

☞单体应用的监控：通过URL:http://hystrix-app:port/hystrix.stream开启，实现对具体某个服务实例的监控。

☞Delay：控制服务器上轮询监控信息的延迟时间，默认为2000毫秒，可以通过配置该属性来降低客户端的网络和CPU消耗。

☞Title:该参数可以展示合适的标题。

修改hystrix-feign服务
添加依赖
```
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```
在HystrixFeignApplication添加配置
```
    @Bean
    public ServletRegistrationBean getServlet(){
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }
```
启动hystrix-feign访问
访问`http://localhost:9004/hystrix`
配置监控服务主机：http://localhost:8087/hystrix.stream

![a1a570dac4cad437109ca230b7d352a](http://520htt.com/upload/2020/05/a1a570dac4cad437109ca230b7d352a-f8942d1ce462440abc5fa64f80086659.png)
点击 Monitor Stream进入如下图:
![f39d8d4dc98f9d4d7913b4444009d88](http://520htt.com/upload/2020/05/f39d8d4dc98f9d4d7913b4444009d88-299dd2921a5c42a5a016196bbadd6953.png)

含义解释：
![微信图片_20200521033854](http://520htt.com/upload/2020/05/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20200521033854-0b8a1e95b33e484a932c8043e9179c5f.png)

 在监控的界面有两个重要的图形信息：一个实心圆和一条曲线。

　　▪实心圆：1、通过颜色的变化代表了实例的健康程度，健康程度从绿色、黄色、橙色、红色递减。2、通过大小表示请求流量发生变化，流量越大该实心圆就越大。所以可以在大量的实例中快速发现故障实例和高压实例。

　　▪曲线：用来记录2分钟内流浪的相对变化，可以通过它来观察流量的上升和下降趋势。


#### Hystrix流程结构解析
![微信图片_20200521034232](http://520htt.com/upload/2020/05/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20200521034232-33b486a27a5045ac921265c7231b89f1.png)
流程说明:

1:每次调用创建一个新的HystrixCommand,把依赖调用封装在run()方法中.

2:执行execute()/queue做同步或异步调用.

3:判断熔断器(circuit-breaker)是否打开,如果打开跳到步骤8,进行降级策略,如果关闭进入步骤.

4:判断线程池/队列/信号量是否跑满，如果跑满进入降级步骤8,否则继续后续步骤.

5:调用HystrixCommand的run方法.运行依赖逻辑

5a:依赖逻辑调用超时,进入步骤8.

6:判断逻辑是否调用成功

6a:返回成功调用结果

6b:调用出错，进入步骤8.

7:计算熔断器状态,所有的运行状态(成功, 失败, 拒绝,超时)上报给熔断器，用于统计从而判断熔断器状态.

8:getFallback()降级逻辑.

  以下四种情况将触发getFallback调用：

 (1):run()方法抛出非HystrixBadRequestException异常。

 (2):run()方法调用超时

 (3):熔断器开启拦截调用

 (4):线程池/队列/信号量是否跑满

8a:没有实现getFallback的Command将直接抛出异常

8b:fallback降级逻辑调用成功直接返回

8c:降级逻辑调用失败抛出异常

9:返回执行成功结果

来源:网络
## 获取更多关注公众号: 
![qrcode_for_gh_c8260435c2d7_258](http://520htt.com/upload/2020/05/qrcode_for_gh_c8260435c2d7_258-4703192829404b63b48d02e6e2cf9e1c.jpg)



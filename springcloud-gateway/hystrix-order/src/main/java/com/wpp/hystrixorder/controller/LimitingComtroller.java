package com.wpp.hystrixorder.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: jackgeeks
 * @ProjectName: springcloud-samples
 * @Package: com.wpp.hystrixorder.controller
 * @ClassName: LimitingComtroller
 * @Description: 限流
 * @CreateDate: 2020/5/21 1:03
 * @Version: 1.0
 */
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
    public String sayHello(HttpServletResponse httpServletResponse) {
        return "Hello World";
    }

    /**
     * 降级方法，状态码返回503
     * 注意，降级方法的返回类型与形参要与原方法相同，可以多一个Throwable参数放到最后，用来获取异常信息
     */
    public String fallbackMethod(HttpServletResponse httpServletResponse, Throwable e) {
        httpServletResponse.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        return e.getMessage();
    }
}

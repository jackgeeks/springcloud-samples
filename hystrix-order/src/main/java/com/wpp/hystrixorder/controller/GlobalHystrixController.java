package com.wpp.hystrixorder.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Author: jackgeeks
 * @ProjectName: springcloud-samples
 * @Package: com.wpp.hystrixorder.controller
 * @ClassName: GlobalHystrixController
 * @Description: @全局降级
 * @CreateDate: 2020/5/20 23:01
 * @Version: 1.0
 */
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

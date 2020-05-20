package com.wpp.hystrixorder.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;


/**
 * @Author: jackgeeks
 * @ProjectName: springcloud-samples
 * @Package: com.wpp.hystrixorder.controller
 * @ClassName: GlobalHystrixController
 * @Description: 局部降级
 * @CreateDate: 2020/5/20 23:01
 * @Version: 1.0
 */
@RestController
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
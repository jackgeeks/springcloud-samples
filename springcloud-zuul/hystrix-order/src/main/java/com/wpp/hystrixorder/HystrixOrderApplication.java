package com.wpp.hystrixorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableHystrix
@EnableDiscoveryClient
public class HystrixOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixOrderApplication.class, args);
    }

}

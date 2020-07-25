package com.wpp.strean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class StreamServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamServerApplication.class, args);
    }

}

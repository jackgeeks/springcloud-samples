package com.wpp.eurekaalone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class StreamConsumerOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamConsumerOneApplication.class, args);
    }

}

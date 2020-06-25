package com.wpp.openfeign.config;

import com.wpp.rule.myrule;
import feign.Logger;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: jackgeeks
 * @ProjectName: springcloud-samples
 * @Package: com.wpp.openfeign.condig
 * @ClassName: FeignConfig
 * @Description: @todo
 * @CreateDate: 2020/5/18 17:35
 * @Version: 1.0
 */
@Configuration
@RibbonClient(name = "payment", configuration = myrule.class)
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
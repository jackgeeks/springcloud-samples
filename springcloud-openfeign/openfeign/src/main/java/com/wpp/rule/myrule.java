package com.wpp.rule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: jackgeeks
 * @ProjectName: springcloud-samples
 * @Package: com.rule
 * @ClassName: myrule
 * @Description: @todo
 * @CreateDate: 2020/5/16 22:10
 * @Version: 1.0
 */
@Configuration
public class myrule {
    @Bean
    public IRule my(){
        return  new RandomRule();//随机
    }
}

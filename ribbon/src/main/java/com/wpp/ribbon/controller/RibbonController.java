package com.wpp.ribbon.controller;

import com.rule.myrule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: jackgeeks
 * @ProjectName: springcloud-samples
 * @Package: com.wpp.ribbon.controller
 * @ClassName: RibbonController
 * @Description: @todo
 * @CreateDate: 2020/5/16 21:50
 * @Version: 1.0
 */
@RestController
@RibbonClient(name = "payment", configuration = myrule.class)
public class RibbonController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("pay")
    public ResponseEntity<String> pay(){
        ResponseEntity<String> entity = restTemplate.getForEntity("http://payment/pay", String.class);

        return  entity;
    }

}

package com.wpp.hystrixorder.controller;

import com.wpp.hystrixorder.service.CircuitBreakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: jackgeeks
 * @ProjectName: springcloud-samples
 * @Package: com.wpp.hystrixorder.controller
 * @ClassName: CircuitBreakerController
 * @Description: 服务熔断
 * @CreateDate: 2020/5/20 23:42
 * @Version: 1.0
 */
@RestController
public class CircuitBreakerController {
    @Autowired
    private CircuitBreakerService CircuitBreaker;


    @GetMapping("/fuse/{id}")
    public ResponseEntity<String> Fuse(@PathVariable ("id") Integer id){
        return  ResponseEntity.ok(CircuitBreaker.CircuitBreaker(id));
    }


}

package com.wpp.hystrixorder.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @Author: jackgeeks
 * @ProjectName: springcloud-samples
 * @Package: com.wpp.hystrixorder.controller
 * @ClassName: HystrixApiController
 * @Description: @todo
 * @CreateDate: 2020/5/20 23:21
 * @Version: 1.0
 */
@RestController
public class HystrixApiController {

    @Value("${server.port}")
    private  String port;
    @GetMapping("/port")
    public ResponseEntity<String> GetPort(){
        return  ResponseEntity.ok(port);
    }
    @GetMapping("/time")
    public ResponseEntity<String> GetTime(){
        return  ResponseEntity.ok( LocalDateTime.now().toString());
    }

}

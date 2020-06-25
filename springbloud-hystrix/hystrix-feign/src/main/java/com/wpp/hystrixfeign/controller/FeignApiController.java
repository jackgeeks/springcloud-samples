package com.wpp.hystrixfeign.controller;

import com.wpp.hystrixfeign.client.HystrixApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: jackgeeks
 * @ProjectName: springcloud-samples
 * @Package: com.wpp.hystrixfeign.controller
 * @ClassName: FeignApiController
 * @Description: @todo
 * @CreateDate: 2020/5/20 23:34
 * @Version: 1.0
 */
@RestController
public class FeignApiController {

    @Resource
    private HystrixApi hystrixapi;

    @GetMapping("/port")
    public ResponseEntity<String> GetPort(){
        return  ResponseEntity.ok(hystrixapi.GetPort());
    }
    @GetMapping("/time")
    public ResponseEntity<String> GetTime(){
        return  ResponseEntity.ok(hystrixapi.GetTime());
    }
    @GetMapping("/fuse/{id}")
    public ResponseEntity<String> Fuse(@PathVariable("id") Integer id){
        return  ResponseEntity.ok(hystrixapi.Fuse(id));
    }

}

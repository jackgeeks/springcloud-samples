package com.wpp.hystrixfeign.client;

import com.wpp.hystrixfeign.client.impl.HystrixApiImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author: jackgeeks
 * @ProjectName: springcloud-samples
 * @Package: com.wpp.hystrixfeign.client
 * @ClassName: HystrixApi
 * @Description: @todo
 * @CreateDate: 2020/5/20 23:21
 * @Version: 1.0
 */
@FeignClient(value = "hystrix-order" ,fallback = HystrixApiImpl.class)
public interface HystrixApi {

    @GetMapping("/port")
    String GetPort();
    @GetMapping("/time")
    String GetTime();
    @GetMapping("/fuse/{id}")
    String Fuse(@PathVariable("id") Integer id);
}

package com.wpp.openfeign.client;

import com.wpp.rule.myrule;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * @Author: jackgeeks
 * @ProjectName: springcloud-samples
 * @Package: com.wpp.openfeign.client
 * @ClassName: PaymentClient
 * @Description: @todo
 * @CreateDate: 2020/5/18 17:31
 * @Version: 1.0
 */
@FeignClient("payment")
public interface PaymentClient {
    @GetMapping("pay")
     String pay();

}

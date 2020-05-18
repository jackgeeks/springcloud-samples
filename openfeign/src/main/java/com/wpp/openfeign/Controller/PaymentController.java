package com.wpp.openfeign.Controller;

import com.wpp.openfeign.client.PaymentClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: jackgeeks
 * @ProjectName: springcloud-samples
 * @Package: com.wpp.payment01.Controller
 * @ClassName: PaymentController
 * @Description: @todo
 * @CreateDate: 2020/5/16 21:37
 * @Version: 1.0
 */
@RestController
public class PaymentController {
  @Autowired
  private PaymentClient paymentClient;
     @GetMapping("/pay")
    public ResponseEntity<String> pay(){
       return  ResponseEntity.ok(paymentClient.pay());
    }

}

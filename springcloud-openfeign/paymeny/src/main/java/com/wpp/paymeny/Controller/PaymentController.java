package com.wpp.paymeny.Controller;

import org.springframework.beans.factory.annotation.Value;
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
    @Value("${server.port}")
    private  String port;
    @GetMapping("pay")
    public ResponseEntity<String> pay(){
      return  ResponseEntity.ok(port);
    }
}

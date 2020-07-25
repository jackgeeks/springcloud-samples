package com.wpp.eurekaalone.Processor;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Author: jackgeeks
 * @ProjectName: springcloud-samples
 * @Package: com.wpp.eurekaalone.controller
 * @ClassName: ll
 * @Description: @todo
 * @CreateDate: 2020/7/5 18:03
 * @Version: 1.0
 */
public interface MyInput {
    String INPUT = "myinput";

    @Input(MyInput.INPUT)
    SubscribableChannel input();
}

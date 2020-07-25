package com.wpp.strean.Processor;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @Author: lyang
 * @Date: 2019/1/1 18:22
 */
public interface MyOutput {
    String OUTPUT = "myoutput";

    @Output(MyOutput.OUTPUT)
    MessageChannel output();
}

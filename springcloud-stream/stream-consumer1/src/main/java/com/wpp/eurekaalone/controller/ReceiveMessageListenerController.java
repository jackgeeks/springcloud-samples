package com.wpp.eurekaalone.controller;
import com.wpp.eurekaalone.Processor.MyProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * @Author: jackgeeks
 * @ProjectName: springcloud-samples
 * @Package: com.wpp.eurekaalone.controller
 * @ClassName: ll
 * @Description: @todo
 * @CreateDate: 2020/7/5 18:03
 * @Version: 1.0
 */
@Component
@EnableBinding(MyProcessor.class)
@Slf4j
public class ReceiveMessageListenerController {
    @Value("${server.port}")
    private String serverPort;

    @StreamListener(MyProcessor.INPUT)
    @SendTo(MyProcessor.CALLBACKINPUT)
    public String process(String message){
        log.info("消费者1号，------->接收到的消息： "+message+"\t port: "+serverPort);
        return message;
    }

    @StreamListener(MyProcessor.CALLBACKINPUT)
    public void callback(String message){
        log.info("message has recived : {} ", message);
    }
}
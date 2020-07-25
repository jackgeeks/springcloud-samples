package com.wpp.eurekaalone.controller;
import com.wpp.eurekaalone.Processor.MyProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * @author wsk
 * @date 2020/3/17 14:24
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
        log.info("消费者2号，------->接收到的消息： "+message+"\t port: "+serverPort);
        return message;
    }

    @StreamListener(MyProcessor.CALLBACKINPUT)
    public void callback(String message){
        log.info("message has recived : {} ", message);
    }
}
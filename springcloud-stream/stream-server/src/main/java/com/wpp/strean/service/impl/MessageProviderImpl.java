package com.wpp.strean.service.impl;

import com.wpp.strean.service.IMessageProvider;
import com.wpp.strean.Processor.MyProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;

import java.util.UUID;


/**
 * @Author: jackgeeks
 * @ProjectName: springcloud-samples
 * @Package: com.wpp.strean.controller
 * @ClassName: ss
 * @Description: @todo
 * @CreateDate: 2020/6/28 0:10
 * @Version: 1.0
 * **/
//这不是传统的service,这是和rabbitmq打交道的，不需要加注解@Service
//这里不掉dao，掉消息中间件的service
//信道channel和exchange绑定在一起
@EnableBinding(Source.class)
public class MessageProviderImpl implements IMessageProvider {

    /**
     * 消息发送管道
     */
    @Autowired
    private MyProcessor processor;

    @Override
    public String send() {
        String serial = UUID.randomUUID().toString();
        processor.output().send(MessageBuilder.withPayload(serial).build());
        System.out.println("serial = " + serial);
        return "ok";
    }
}
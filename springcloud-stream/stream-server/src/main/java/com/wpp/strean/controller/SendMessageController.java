package com.wpp.strean.controller;

import com.wpp.strean.service.IMessageProvider;
import com.wpp.strean.Processor.MyProcessor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @Author: jackgeeks
 * @ProjectName: springcloud-samples
 * @Package: com.wpp.strean.controller
 * @ClassName: ss
 * @Description: @todo
 * @CreateDate: 2020/6/28 0:10
 * @Version: 1.0**/
@RestController
@EnableBinding(MyProcessor.class)
public class SendMessageController {

    @Resource
    private IMessageProvider messageProvider;


    @GetMapping("/sendMessage")
    public String sendMessage(){
        return messageProvider.send();
    }
}
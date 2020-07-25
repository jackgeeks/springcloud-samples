package com.wpp.strean.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

/**
 * @Author: jackgeeks
 * @ProjectName: springcloud-samples
 * @Package: com.wpp.strean.controller
 * @ClassName: ss
 * @Description: @todo
 * @CreateDate: 2020/6/28 0:10
 * @Version: 1.0
 * **/
public interface IMessageProvider {
    /**
     * 消息发送
     * @return
     */
    String send();
}
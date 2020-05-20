package com.wpp.hystrixfeign.client.impl;

import com.netflix.ribbon.proxy.annotation.Hystrix;
import com.wpp.hystrixfeign.client.HystrixApi;
import org.springframework.stereotype.Component;

/**
 * @Author: jackgeeks
 * @ProjectName: springcloud-samples
 * @Package: com.wpp.hystrixfeign.client.impl
 * @ClassName: HystrixApiImpl
 * @Description: @todo
 * @CreateDate: 2020/5/20 23:30
 * @Version: 1.0
 */
@Component
public class HystrixApiImpl implements HystrixApi {
    @Override
    public String GetPort() {
        return "接口异常，请稍后再试，o(╥﹏╥)o";
    }

    @Override
    public String GetTime() {
        return "接口异常，请稍后再试，o(╥﹏╥)o";
    }

    @Override
    public String Fuse(Integer id) {

            return "接口异常，请稍后再试，o(╥﹏╥)o";
        }
}

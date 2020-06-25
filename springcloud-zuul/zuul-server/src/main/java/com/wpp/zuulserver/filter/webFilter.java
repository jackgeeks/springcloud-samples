package com.wpp.zuulserver.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: jackgeeks
 * @ProjectName: springcloud-samples
 * @Package: com.wpp.zuulserver.filter
 * @ClassName: webFilter
 * @Description: @todo
 * @CreateDate: 2020/5/22 23:36
 * @Version: 1.0
 */
@Component
@Slf4j
public class webFilter extends ZuulFilter {
    @Override
    public String filterType() {
        // 登录校验，肯定是在前置拦截
        return "pre";
    }

    @Override
    public int filterOrder() {
        // 顺序设置为1
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        // 返回true，代表过滤器生效。
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        // 从上下文中获取request对象
        HttpServletRequest req = ctx.getRequest();
        log.info("HttpServletRequest对象"+req);


        // 校验通过，可以考虑把用户信息放入上下文，继续向后执行
        return null;
    }
}

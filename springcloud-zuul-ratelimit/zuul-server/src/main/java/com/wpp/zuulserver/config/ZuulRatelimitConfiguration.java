package com.wpp.zuulserver.config;

import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.DefaultRateLimiterErrorHandler;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.RateLimiterErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Gjing
 **/
@Configuration
@Slf4j
public class ZuulRatelimitConfiguration {

    @Bean
    public RateLimiterErrorHandler rateLimitErrorHandler() {
        return new DefaultRateLimiterErrorHandler() {
            @Override
            public void handleSaveError(String key, Exception e) {
                log.info("handleSaveError-->"+key);
                // custom code
            }

            @Override
            public void handleFetchError(String key, Exception e) {
                log.info("handleFetchError-->"+key);
                // custom code
            }

            @Override
            public void handleError(String msg, Exception e) {
                log.info("handleError-->"+msg);
                // custom code
            }
        };
    }
}

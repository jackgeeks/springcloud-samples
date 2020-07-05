package com.wpp.getewayserver.config;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GateWayConfig {
    /**
     * 配置一个id为route-name的路由规则，
     * 当访问地址http://localhost:8089/archives时会自动转发到地址：http://520htt.com/archives
     * @param routeLocatorBuilder
     * @return
     */
    @SuppressWarnings("JavaDoc")
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();

        routes.route("order",
                r -> r.path("/archives")
                        .uri("http://520htt.com/archives")).build();
        return routes.build();
    }
}
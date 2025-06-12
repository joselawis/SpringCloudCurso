package com.lawis.springcloud.app.gateway.router;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.stripPrefix;
import static org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions.circuitBreaker;
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class RouterConfig {

    @Bean
    RouterFunction<ServerResponse> router() {
        return route("msvc-products")
                .route(path("/api/products/**"), http())
                .filter((request, next) -> {
                    ServerRequest requestModified = ServerRequest.from(request)
                            .header("message-request", "Message from msvc-gateway-server to msvc-products")
                            .build();
                    ServerResponse response = next.handle(requestModified);
                    response.headers().add("message-response",
                            "Message from msvc-gateway-server (and msvc-products) to client");
                    return response;
                })
                .filter(lb("msvc-products"))
                .filter(circuitBreaker(config -> config.setId("products")
                        .setStatusCodes(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .setFallbackPath("forward:/api/items/5")))
                .before(stripPrefix(2))
                .build();
    }
}

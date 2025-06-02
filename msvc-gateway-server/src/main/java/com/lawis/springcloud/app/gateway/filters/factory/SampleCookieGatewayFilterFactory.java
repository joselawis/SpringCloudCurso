package com.lawis.springcloud.app.gateway.filters.factory;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

@Component
public class SampleCookieGatewayFilterFactory
        extends AbstractGatewayFilterFactory<SampleCookieGatewayFilterFactory.ConfigurationCookie> {

    private final Logger logger = LoggerFactory.getLogger(SampleCookieGatewayFilterFactory.class);

    public SampleCookieGatewayFilterFactory() {
        super(ConfigurationCookie.class);
    }

    @Override
    public GatewayFilter apply(ConfigurationCookie config) {

        GatewayFilter filter = (exchange, chain) -> {
            logger.info("Executing pre gateway filter factory: " + config.message);

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                Optional.ofNullable(config.value).ifPresent(cookie -> {
                    exchange.getResponse()
                            .addCookie(ResponseCookie.from(config.name, cookie).build());
                });

                logger.info("Executing post gateway filter factory: " + config.message);
            }));
        };

        return new OrderedGatewayFilter(filter, 100);
    }

    @Override
    public String name() {
        return "CookieExampleFilter";
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return List.of("name", "value", "message");
    }

    @Getter
    @Setter
    public static class ConfigurationCookie {
        private String name;
        private String value;
        private String message;
    }
}

package com.lawis.springcloud.app.gateway.filters;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

@Component
public class SampleGlobalFilter implements Filter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(SampleGlobalFilter.class);

    @Override
    public int getOrder() {
        return 100;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Log before request processing
        logger.info("Executing filter before request PRE processing");

        // Add a header "token" to the request (not directly possible in Servlet API, so
        // just log)
        // In Servlet API, request headers are immutable, so you can't add headers
        // directly.
        // You would need to wrap the request if you want to modify headers.

        // Continue filter chain
        chain.doFilter(request, response);

        // Log after response processing
        logger.info("Executing POST filter response");

        // Try to get "token" from request headers (not directly possible, so just log)
        String token = request.getParameter("token"); // As an example, get from parameter

        if (token != null) {
            logger.info("Token: {}", token);
            // Add token to response header
            if (response instanceof jakarta.servlet.http.HttpServletResponse httpResp) {
                httpResp.addHeader("token", token);
            }
        }

        // Add a cookie "color=red" to the response
        if (response instanceof jakarta.servlet.http.HttpServletResponse httpResp) {
            jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("color", "red");
            httpResp.addCookie(cookie);
            httpResp.setContentType("text/plain");
        }
    }
}

// import java.util.Optional;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.cloud.gateway.filter.GatewayFilterChain;
// import org.springframework.cloud.gateway.filter.GlobalFilter;
// import org.springframework.core.Ordered;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseCookie;
// import org.springframework.http.server.reactive.ServerHttpRequest;
// import org.springframework.stereotype.Component;
// import org.springframework.web.server.ServerWebExchange;
// import reactor.core.publisher.Mono;

// @Component
// public class SampleGlobalFilter implements GlobalFilter, Ordered {

// private final Logger logger =
// LoggerFactory.getLogger(SampleGlobalFilter.class);

// @Override
// public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain
// chain) {

// logger.info("Executing filter before request PRE processing");

// ServerHttpRequest mutatedRequest = exchange.getRequest()
// .mutate()
// .header("token", "abcdefg")
// .build();

// ServerWebExchange mutatedExchange = exchange.mutate()
// .request(mutatedRequest)
// .build();

// return chain.filter(mutatedExchange).then(Mono.fromRunnable(() -> {
// logger.info("Executing POST filter response");

// String token = mutatedExchange.getRequest()
// .getHeaders()
// .getFirst("token");

// Optional.ofNullable(token).ifPresent(value -> {
// logger.info("Token: " + value);

// mutatedExchange.getResponse()
// .getHeaders()
// .add("token", value);
// });

// exchange.getResponse()
// .getCookies()
// .add("color", ResponseCookie.from("color", "red").build());
// exchange.getResponse()
// .getHeaders()
// .setContentType(MediaType.TEXT_PLAIN);
// }));

// }

// @Override

// public int getOrder() {
// return 100;
// }
// }

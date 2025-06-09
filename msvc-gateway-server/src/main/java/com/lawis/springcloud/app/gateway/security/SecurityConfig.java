package com.lawis.springcloud.app.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CorsSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http.authorizeExchange(authorize -> authorize
                .pathMatchers("/authorized", "/logout").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/items", "/api/products", "/api/users").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/items/{id}", "/api/products/{id}", "/api/users/{id}")
                .hasAnyRole("ADMIN", "USER")
                .pathMatchers("/api/items/**", "/api/products/**", "/api/users/**").hasRole("ADMIN")
                .anyExchange().authenticated())
                .cors(CorsSpec::disable)
                .oauth2Login(Customizer.withDefaults())
                .oauth2Client(Customizer.withDefaults())
                .oauth2ResourceServer(Customizer.withDefaults())
                .build();
    }
}

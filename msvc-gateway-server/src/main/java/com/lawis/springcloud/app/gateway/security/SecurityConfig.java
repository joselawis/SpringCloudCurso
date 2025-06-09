package com.lawis.springcloud.app.gateway.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CorsSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfig {

    private static final String ROLE_USER = "USER";
    private static final String ROLE_ADMIN = "ADMIN";

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http.authorizeExchange(authorize -> authorize
                .pathMatchers("/authorized", "/logout").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/items", "/api/products", "/api/users").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/items/{id}", "/api/products/{id}", "/api/users/{id}")
                .hasAnyRole(ROLE_ADMIN, ROLE_USER)
                .pathMatchers("/api/items/**", "/api/products/**", "/api/users/**").hasRole(ROLE_ADMIN)
                .anyExchange().authenticated())
                .cors(CorsSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .oauth2Login(Customizer.withDefaults())
                .oauth2Client(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(this::jwtConverter))
                .build();
    }

    private JwtSpec jwtConverter(JwtSpec jwt) {
        return jwt.jwtAuthenticationConverter(new Converter<Jwt, Mono<AbstractAuthenticationToken>>() {
            @Override
            @Nullable
            public Mono<AbstractAuthenticationToken> convert(Jwt source) {
                Collection<GrantedAuthority> authorities = source.getClaimAsStringList("roles").stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                return Mono.just(new JwtAuthenticationToken(source, authorities));
            }

        });
    }
}

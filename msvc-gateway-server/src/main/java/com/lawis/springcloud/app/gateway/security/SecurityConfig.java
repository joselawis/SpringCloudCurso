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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private static final String ROLE_USER = "USER";
    private static final String ROLE_ADMIN = "ADMIN";

    @Bean
    SecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/authorized", "/logout").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/items", "/api/products", "/api/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/items/{id}", "/api/products/{id}", "/api/users/{id}")
                .hasAnyRole(ROLE_ADMIN, ROLE_USER)
                .requestMatchers("/api/items/**", "/api/products/**", "/api/users/**").hasRole(ROLE_ADMIN)
                .anyRequest().authenticated())
                .cors(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(login -> login.loginPage("/oauth2/authorization/client-app"))
                .oauth2Client(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(this::jwtConverter))
                .build();
    }

    private OAuth2ResourceServerConfigurer<HttpSecurity>.JwtConfigurer jwtConverter(
            OAuth2ResourceServerConfigurer<HttpSecurity>.JwtConfigurer jwt) {
        return jwt.jwtAuthenticationConverter(new Converter<Jwt, AbstractAuthenticationToken>() {
            @Override
            @Nullable
            public AbstractAuthenticationToken convert(Jwt source) {
                Collection<GrantedAuthority> authorities = source.getClaimAsStringList("roles").stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                return new JwtAuthenticationToken(source, authorities);
            }
        });
    }

    // private JwtSpec jwtConverter(JwtSpec jwt) {
    // return jwt.jwtAuthenticationConverter(new Converter<Jwt,
    // Mono<AbstractAuthenticationToken>>() {
    // @Override
    // @Nullable
    // public Mono<AbstractAuthenticationToken> convert(Jwt source) {
    // Collection<GrantedAuthority> authorities =
    // source.getClaimAsStringList("roles").stream()
    // .map(SimpleGrantedAuthority::new)
    // .collect(Collectors.toList());
    // return Mono.just(new JwtAuthenticationToken(source, authorities));
    // }
    // });
    // }
}

package com.lawis.springcloud.msvc.oauth.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.lawis.libs.msvc.commons.models.User;

import io.micrometer.tracing.Tracer;

@Service
public class UsersService implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(UsersService.class);

    private final WebClient client;

    private final Tracer tracer;

    public UsersService(WebClient client, Tracer tracer) {
        this.client = client;
        this.tracer = tracer;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Entering process of loging UsersService::loadUserByUsername() - username: {}", username);
        Map<String, String> params = new HashMap<>();
        params.put("username", username);

        try {
            User user = client.get()
                    .uri("/username/{username}", params)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();

            if (user == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }

            List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .toList();

            logger.info("Login Success - User found: {}", user.getUsername());
            tracer.currentSpan().tag("success.login", "Login Success - User found: " + user.getUsername());
            return new org.springframework.security.core.userdetails.User(user.getUsername(),
                    user.getPassword(),
                    user.getEnabled(),
                    true,
                    true,
                    true,
                    authorities);
        } catch (WebClientResponseException e) {
            String errorMessage = "Error while trying to login user " + username;
            logger.error(errorMessage);
            tracer.currentSpan().tag("error.login.message", errorMessage + " - " + e.getMessage());
            throw new UsernameNotFoundException(errorMessage, e);
        }
    }

}

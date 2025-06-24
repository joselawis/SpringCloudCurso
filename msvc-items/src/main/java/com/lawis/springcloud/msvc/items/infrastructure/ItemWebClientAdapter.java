package com.lawis.springcloud.msvc.items.infrastructure;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.lawis.libs.msvc.commons.models.Product;
import com.lawis.springcloud.msvc.items.application.ProductOutPort;

@Service
public class ItemWebClientAdapter implements ProductOutPort {

    private final String pathVariableId;

    private final WebClient client;

    public ItemWebClientAdapter(WebClient client,
            @Value("${itemservice.path-variable-id:/{id}}") String pathVariableId) {
        this.client = client;
        this.pathVariableId = pathVariableId;
    }

    @Override
    public List<Product> findAll() {
        return client.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Product.class)
                .collectList()
                .block();
    }

    @Override
    public Optional<Product> findById(Long id) {
        Map<String, Object> params = Map.of("id", id);
        return Optional.ofNullable(client.get()
                .uri(pathVariableId, params)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Product.class)
                .block());
    }

    @Override
    public Product save(Product product) {
        return client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class)
                .block();
    }

    @Override
    public Optional<Product> update(Product product, Long id) {
        Map<String, Object> params = Map.of("id", id);
        return Optional.ofNullable(client.put()
                .uri(pathVariableId, params)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class)
                .block());
    }

    @Override
    public Boolean delete(Long id) {
        Map<String, Object> params = Map.of("id", id);
        return client.delete()
                .uri(pathVariableId, params)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

}

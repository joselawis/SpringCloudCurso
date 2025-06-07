package com.lawis.springcloud.msvc.items.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import com.lawis.libs.msvc.commons.entities.Product;
import com.lawis.springcloud.msvc.items.models.Item;

@Primary
@Service
public class ItemServiceWebClient implements ItemService {

    private static final String PATH_VARIABLE_ID = "/{id}";

    private final WebClient.Builder client;

    private Random random = new Random();

    public ItemServiceWebClient(Builder client) {
        this.client = client;
    }

    @Override
    public List<Item> findAll() {
        return client.build()
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Product.class)
                .map(product -> new Item(product, random.nextInt(0, 10) + 1))
                .collectList()
                .block();
    }

    @Override
    public Optional<Item> findById(Long id) {
        Map<String, Object> params = Map.of("id", id);

        return Optional.ofNullable(client.build()
                .get()
                .uri(PATH_VARIABLE_ID, params)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Product.class)
                .map(product -> new Item(product, random.nextInt(0, 10) + 1))
                .block());
    }

    @Override
    public Product save(Product product) {
        return client.build()
                .post()
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

        return Optional.ofNullable(client.build()
                .put()
                .uri(PATH_VARIABLE_ID, params)
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

        return client.build()
                .delete()
                .uri(PATH_VARIABLE_ID, params)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

}

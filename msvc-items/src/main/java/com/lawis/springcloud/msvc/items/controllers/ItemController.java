package com.lawis.springcloud.msvc.items.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.lawis.springcloud.msvc.items.models.Item;
import com.lawis.springcloud.msvc.items.models.Product;
import com.lawis.springcloud.msvc.items.services.ItemService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ItemController {

    private final Logger logger = LoggerFactory.getLogger(ItemController.class);
    private final ItemService service;
    private final CircuitBreakerFactory cBreakerFactory;

    public ItemController(@Qualifier("itemServiceWebClient") ItemService service,
            CircuitBreakerFactory cBreakerFactory) {
        this.service = service;
        this.cBreakerFactory = cBreakerFactory;
    }

    @GetMapping
    public ResponseEntity<List<Item>> list(@RequestParam(required = false) String name,
            @RequestHeader(name = "token-request", required = false) String token) {
        System.out.println("Token from request header: " + token);
        System.out.println("Name from request parameter: " + name);
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable Long id) {
        // Optional<Item> itemOptional = service.findById(id);
        Optional<Item> itemOptional = cBreakerFactory.create("items").run(
                () -> service.findById(id),
                throwable -> {
                    logger.error(throwable.getMessage());
                    Product product = new Product();
                    product.setId(1L);
                    product.setName("Default Product");
                    product.setPrice(100.0);
                    product.setCreatedAt(LocalDate.now());
                    return Optional.of(new Item(product, 5));
                });
        if (itemOptional.isPresent()) {
            return ResponseEntity.ok(itemOptional.orElseThrow());
        }
        return ResponseEntity
                .status(404)
                .body(Collections.singletonMap("message", "Product doesn't exist in msvc-product"));
    }

}

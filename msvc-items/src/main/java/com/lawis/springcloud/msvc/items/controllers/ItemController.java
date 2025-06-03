package com.lawis.springcloud.msvc.items.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.lawis.springcloud.msvc.items.models.Item;
import com.lawis.springcloud.msvc.items.models.Product;
import com.lawis.springcloud.msvc.items.services.ItemService;

import feign.Response;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

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

    @CircuitBreaker(name = "items", fallbackMethod = "getFallbackMethodProduct")
    @GetMapping("/details2/{id}")
    public ResponseEntity<?> details2(@PathVariable Long id) {
        Optional<Item> itemOptional = service.findById(id);
        if (itemOptional.isPresent()) {
            return ResponseEntity.ok(itemOptional.orElseThrow());
        }
        return ResponseEntity
                .status(404)
                .body(Collections.singletonMap("message", "Product doesn't exist in msvc-product"));
    }

    @CircuitBreaker(name = "items", fallbackMethod = "getFallbackMethodProduct2")
    @TimeLimiter(name = "items")
    @GetMapping("/details3/{id}")
    public CompletableFuture<?> details3(@PathVariable Long id) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Item> itemOptional = service.findById(id);
            if (itemOptional.isPresent()) {
                return ResponseEntity.ok(itemOptional.orElseThrow());
            }
            return ResponseEntity
                    .status(404)
                    .body(Collections.singletonMap("message", "Product doesn't exist in msvc-product"));
        });
    }

    @SuppressWarnings("unused")
    private ResponseEntity<?> getFallbackMethodProduct(Throwable e) {
        logger.error(e.getMessage());
        Product product = new Product();
        product.setId(1L);
        product.setName("Default Product");
        product.setPrice(100.0);
        product.setCreatedAt(LocalDate.now());
        return ResponseEntity.ok(new Item(product, 5));
    }

    @SuppressWarnings("unused")
    private CompletableFuture<?> getFallbackMethodProduct2(Throwable e) {
        return CompletableFuture.supplyAsync(() -> {
            logger.error(e.getMessage());
            Product product = new Product();
            product.setId(1L);
            product.setName("Default Product");
            product.setPrice(100.0);
            product.setCreatedAt(LocalDate.now());
            return ResponseEntity.ok(new Item(product, 5));
        });
    }
}

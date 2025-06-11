package com.lawis.springcloud.msvc.items.controllers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lawis.libs.msvc.commons.models.Product;
import com.lawis.springcloud.msvc.items.models.Item;
import com.lawis.springcloud.msvc.items.services.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RefreshScope
@RestController
public class ItemController {

    private static final String DEFAULT_PRODUCT = "Default Product";
    private static final String PRODUCT_DOESN_T_EXIST_IN_MSVC_PRODUCT = "Product doesn't exist in msvc-product";
    private static final String MESSAGE = "message";
    private final Logger logger = LoggerFactory.getLogger(ItemController.class);
    private final ItemService service;
    private final CircuitBreakerFactory<?, ?> cBreakerFactory;

    @Value("${configuration.text}")
    private String text;

    private final Environment env;

    public ItemController(@Qualifier("itemServiceFeign") ItemService service,
            CircuitBreakerFactory<?, ?> cBreakerFactory, Environment env) {
        this.service = service;
        this.cBreakerFactory = cBreakerFactory;
        this.env = env;
    }

    @GetMapping("/fetch-configs")
    public ResponseEntity<Map<String, String>> fetchConfigs(@Value("${server.port}") String port) {
        logger.info("Calling controller method ItemController::fetchConfigs()");
        Map<String, String> configs = new HashMap<>();
        configs.put("text", text);
        configs.put("port", port);
        logger.info(port);
        logger.info(text);

        if (env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")) {
            configs.put("author.name", env.getProperty("configuration.author.name", "Default Author"));
            configs.put("author.email", env.getProperty("configuration.author.email", "Default Email"));
        }
        return ResponseEntity.ok(configs);
    }

    @GetMapping
    public ResponseEntity<List<Item>> list(@RequestParam(required = false) String name,
            @RequestHeader(name = "token-request", required = false) String token) {
        logger.info("Calling controller method ItemController::list()");
        logger.info("Token from request header: {}", token);
        logger.info("Name from request parameter: {}", name);
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable Long id) {
        logger.info("Calling controller method ItemController::details() - id: {}", id);
        Optional<Item> itemOptional = cBreakerFactory.create("items").run(
                () -> service.findById(id),
                throwable -> {
                    logger.error(throwable.getMessage());
                    Product product = new Product();
                    product.setId(1L);
                    product.setName(DEFAULT_PRODUCT);
                    product.setPrice(100.0);
                    product.setCreateAt(LocalDate.now());
                    return Optional.of(new Item(product, 5));
                });
        if (itemOptional.isPresent()) {
            return ResponseEntity.ok(itemOptional.orElseThrow());
        }
        return ResponseEntity
                .status(404)
                .body(Collections.singletonMap(MESSAGE, PRODUCT_DOESN_T_EXIST_IN_MSVC_PRODUCT));
    }

    @CircuitBreaker(name = "items", fallbackMethod = "getFallbackMethodProduct")
    @GetMapping("/details2/{id}")
    public ResponseEntity<?> details2(@PathVariable Long id) {
        logger.info("Calling controller method ItemController::details2() - id: {}", id);
        Optional<Item> itemOptional = service.findById(id);
        if (itemOptional.isPresent()) {
            return ResponseEntity.ok(itemOptional.orElseThrow());
        }
        return ResponseEntity
                .status(404)
                .body(Collections.singletonMap(MESSAGE, PRODUCT_DOESN_T_EXIST_IN_MSVC_PRODUCT));
    }

    @CircuitBreaker(name = "items", fallbackMethod = "getFallbackMethodProduct2")
    @TimeLimiter(name = "items")
    @GetMapping("/details3/{id}")
    public CompletableFuture<?> details3(@PathVariable Long id) {
        logger.info("Calling controller method ItemController::details3() - id: {}", id);
        return CompletableFuture.supplyAsync(() -> {
            Optional<Item> itemOptional = service.findById(id);
            if (itemOptional.isPresent()) {
                return ResponseEntity.ok(itemOptional.orElseThrow());
            }
            return ResponseEntity
                    .status(404)
                    .body(Collections.singletonMap(MESSAGE, PRODUCT_DOESN_T_EXIST_IN_MSVC_PRODUCT));
        });
    }

    @SuppressWarnings("unused")
    private ResponseEntity<?> getFallbackMethodProduct(Throwable e) {
        logger.error(e.getMessage());
        Product product = new Product();
        product.setId(1L);
        product.setName(DEFAULT_PRODUCT);
        product.setPrice(100.0);
        product.setCreateAt(LocalDate.now());
        return ResponseEntity.ok(new Item(product, 5));
    }

    @SuppressWarnings("unused")
    private CompletableFuture<?> getFallbackMethodProduct2(Throwable e) {
        return CompletableFuture.supplyAsync(() -> {
            logger.error(e.getMessage());
            Product product = new Product();
            product.setId(1L);
            product.setName(DEFAULT_PRODUCT);
            product.setPrice(100.0);
            product.setCreateAt(LocalDate.now());
            return ResponseEntity.ok(new Item(product, 5));
        });
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Product> create(@RequestBody Product product) {
        logger.info("Calling controller method ItemController::create() - product: {}", product);
        return ResponseEntity.ok(service.save(product));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Product> update(@RequestBody Product product, @PathVariable Long id) {
        logger.info("Calling controller method ItemController::update() - id: {} product: {}", id, product);
        return service.update(product, id).map(
                updatedProduct -> ResponseEntity.status(HttpStatus.CREATED).body(updatedProduct))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        logger.info("Calling controller method ItemController::delete() - id: {}", id);
        return service.delete(id)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}

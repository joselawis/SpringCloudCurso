package com.lawis.springcloud.msvc.products.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.lawis.libs.msvc.commons.models.Product;
import com.lawis.springcloud.msvc.products.services.ProductService;

import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping
public class ProductController {
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Product>> list() {
        logger.info("Calling controller method ItemController::list()");
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> details(@PathVariable Long id) throws NotFoundException, InterruptedException {
        logger.info("Calling controller method ItemController::details() - id: {}", id);
        if (id == 100L) {
            throw new NotFoundException("Invalid product ID: " + id);
        }
        if (id == 7L) {
            TimeUnit.SECONDS.sleep(3L);
        }

        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        logger.info("Calling controller method ItemController::create() - product: {}", product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
        logger.info("Calling controller method ItemController::update() - id: {} product: {}", id, product);
        return service.update(id, product)
                .map(updatedProduct -> ResponseEntity.status(HttpStatus.CREATED).body(updatedProduct))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        logger.info("Calling controller method ItemController::delete() - id: {}", id);
        Optional<Product> productOptional = service.findById(id);
        if (productOptional.isPresent()) {
            service.deleteById(id);
            return ResponseEntity.ok().body(true);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
    }

}

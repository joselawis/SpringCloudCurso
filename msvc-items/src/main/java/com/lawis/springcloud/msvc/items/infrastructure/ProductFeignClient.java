package com.lawis.springcloud.msvc.items.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lawis.libs.msvc.commons.models.Product;

@FeignClient(name = "msvc-products")
public interface ProductFeignClient {

    @GetMapping
    List<Product> findAll();

    @GetMapping("/{id}")
    public Optional<Product> details(@PathVariable Long id);

    @PostMapping
    public Product save(@RequestBody Product product);

    @PutMapping("/{id}")
    public Optional<Product> update(@RequestBody Product product, @PathVariable Long id);

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id);
}

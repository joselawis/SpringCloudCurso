package com.lawis.springcloud.msvc.items.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.lawis.libs.msvc.commons.models.Product;
import com.lawis.springcloud.msvc.items.application.ProductOutPort;

import feign.FeignException;

@Primary
@Service
public class ItemFeignClientAdapter implements ProductOutPort {

    private final ProductFeignClient client;

    public ItemFeignClientAdapter(ProductFeignClient client) {
        this.client = client;
    }

    @Override
    public List<Product> findAll() {
        return client.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return client.details(id);
    }

    @Override
    public Product save(Product product) {
        return client.save(product);
    }

    @Override
    public Optional<Product> update(Product product, Long id) {
        try {
            return client.update(product, id);
        } catch (FeignException e) {
            return Optional.empty();
        }
    }

    @Override
    public Boolean delete(Long id) {
        try {
            return client.delete(id);
        } catch (FeignException e) {
            return false;
        }
    }

}

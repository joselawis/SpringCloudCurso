package com.lawis.springcloud.msvc.items.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.lawis.libs.msvc.commons.entities.Product;
import com.lawis.springcloud.msvc.items.clients.ProductFeignClient;
import com.lawis.springcloud.msvc.items.models.Item;

import feign.FeignException;

@Service
public class ItemServiceFeign implements ItemService {

    private final ProductFeignClient client;

    private Random random = new Random();

    public ItemServiceFeign(ProductFeignClient client) {
        this.client = client;
    }

    @Override
    public List<Item> findAll() {
        return client.findAll().stream()
                .map(product -> new Item(product, random.nextInt(0, 10) + 1))
                .toList();
    }

    @Override
    public Optional<Item> findById(Long id) {
        return client.details(id)
                .map(product -> new Item(product, random.nextInt(0, 10) + 1));
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

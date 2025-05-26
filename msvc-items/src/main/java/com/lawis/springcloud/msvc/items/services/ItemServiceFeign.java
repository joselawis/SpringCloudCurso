package com.lawis.springcloud.msvc.items.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.lawis.springcloud.msvc.items.clients.ProductFeignClient;
import com.lawis.springcloud.msvc.items.models.Item;
import com.lawis.springcloud.msvc.items.models.Product;

import feign.FeignException;

@Service
public class ItemServiceFeign implements ItemService {

    final private ProductFeignClient client;

    public ItemServiceFeign(ProductFeignClient client) {
        this.client = client;
    }

    @Override
    public List<Item> findAll() {
        return client.findAll().stream()
                .map(product -> new Item(product, new Random().nextInt(0, 10) + 1))
                .toList();
    }

    @Override
    public Optional<Item> findById(Long id) {
        try {
            Product product = client.details(id);

            return Optional.of(new Item(
                    product,
                    new Random().nextInt(0, 10) + 1));
        } catch (FeignException e) {
            return Optional.empty();
        }
    }

}

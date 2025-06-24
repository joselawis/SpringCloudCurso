package com.lawis.springcloud.msvc.items.application;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.lawis.libs.msvc.commons.models.Product;
import com.lawis.springcloud.msvc.items.domain.Item;

@Service
public class ItemInAdapter implements ItemInPort {

    private final ProductOutPort outPort;

    private Random random = new Random();

    public ItemInAdapter(ProductOutPort outPort) {
        this.outPort = outPort;
    }

    @Override
    public List<Item> findAll() {
        return outPort.findAll().stream()
                .map(product -> new Item(product, random.nextInt(0, 10) + 1))
                .toList();
    }

    @Override
    public Optional<Item> findById(Long id) {
        return outPort.findById(id)
                .map(product -> new Item(product, random.nextInt(0, 10) + 1));
    }

    @Override
    public Product save(Product product) {
        return outPort.save(product);
    }

    @Override
    public Optional<Product> update(Product product, Long id) {
        return outPort.update(product, id);
    }

    @Override
    public Boolean delete(Long id) {
        return outPort.delete(id);
    }

}

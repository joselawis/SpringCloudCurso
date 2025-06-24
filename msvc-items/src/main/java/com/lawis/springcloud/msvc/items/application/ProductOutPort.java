package com.lawis.springcloud.msvc.items.application;

import java.util.List;
import java.util.Optional;

import com.lawis.libs.msvc.commons.models.Product;

public interface ProductOutPort {

    List<Product> findAll();

    Optional<Product> findById(Long id);

    Product save(Product product);

    Optional<Product> update(Product product, Long id);

    Boolean delete(Long id);
}
package com.lawis.springcloud.msvc.products.services;

import java.util.List;
import java.util.Optional;

import com.lawis.libs.msvc.commons.models.Product;

public interface ProductService {

    List<Product> findAll();

    Optional<Product> findById(Long id);

    Product save(Product product);

    Optional<Product> update(Long id, Product product);

    void deleteById(Long id);

}

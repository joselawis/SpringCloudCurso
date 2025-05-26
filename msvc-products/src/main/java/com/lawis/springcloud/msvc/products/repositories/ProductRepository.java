package com.lawis.springcloud.msvc.products.repositories;

import org.springframework.data.repository.CrudRepository;

import com.lawis.springcloud.msvc.products.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

}

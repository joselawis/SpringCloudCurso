package com.lawis.springcloud.msvc.products.repositories;

import org.springframework.data.repository.CrudRepository;

import com.lawis.libs.msvc.commons.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

}

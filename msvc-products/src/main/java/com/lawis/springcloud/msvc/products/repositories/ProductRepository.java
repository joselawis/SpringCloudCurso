package com.lawis.springcloud.msvc.products.repositories;

import org.springframework.data.repository.CrudRepository;

import com.lawis.libs.msvc.entities.entities.products.ProductEntity;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {

}

package com.lawis.springcloud.msvc.products.mappers;

import org.mapstruct.Mapper;

import com.lawis.libs.msvc.commons.models.Product;
import com.lawis.libs.msvc.entities.products.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toModel(ProductEntity entity);

    ProductEntity toEntity(Product model);
}

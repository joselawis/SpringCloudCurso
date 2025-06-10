package com.lawis.springcloud.msvc.products.services;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lawis.libs.msvc.commons.models.Product;
import com.lawis.libs.msvc.entities.entities.products.ProductEntity;
import com.lawis.springcloud.msvc.products.mappers.ProductMapper;
import com.lawis.springcloud.msvc.products.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final Environment environment;
    private final ProductMapper mapper;

    public ProductServiceImpl(ProductRepository repository, Environment environment, ProductMapper mapper) {
        this.repository = repository;
        this.environment = environment;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return ((Collection<ProductEntity>) repository.findAll()).stream()
                .map(mapper::toModel)
                .map(product -> {
                    product.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
                    return product;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toModel)
                .map(product -> {
                    product.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
                    return product;
                });
    }

    @Override
    @Transactional
    public Product save(Product product) {
        ProductEntity productEntity = mapper.toEntity(product);
        ProductEntity productSaved = repository.save(productEntity);
        return mapper.toModel(productSaved);
    }

    @Override
    @Transactional
    public Optional<Product> update(Long id, Product product) {
        return repository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setCreateAt(product.getCreateAt());
                    return repository.save(existingProduct);
                })
                .map(mapper::toModel);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}

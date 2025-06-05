package com.lawis.springcloud.msvc.products.services;

import java.util.List;
import java.util.Optional;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lawis.springcloud.msvc.products.entities.Product;
import com.lawis.springcloud.msvc.products.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final Environment environment;

    public ProductServiceImpl(ProductRepository repository, Environment environment) {
        this.repository = repository;
        this.environment = environment;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return ((List<Product>) repository.findAll()).stream()
                .peek(product -> product.setPort(Integer.parseInt(environment.getProperty("local.server.port"))))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return repository.findById(id).map(product -> {
            product.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
            return product;
        });
    }

    @Override
    @Transactional
    public Product save(Product product) {
        return repository.save(product);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}

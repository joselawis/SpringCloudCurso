package com.lawis.springcloud.msvc.items.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.lawis.springcloud.msvc.items.models.Item;
import com.lawis.springcloud.msvc.items.services.ItemService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class ItemController {

    private final ItemService service;

    public ItemController(@Qualifier("itemServiceWebClient") ItemService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Item>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable Long id) {
        Optional<Item> itemOptional = service.findById(id);
        if (itemOptional.isPresent()) {
            return ResponseEntity.ok(itemOptional.orElseThrow());
        }
        return ResponseEntity
                .status(404)
                .body(Collections.singletonMap("message", "Product doesn't exist in msvc-product"));
    }

}

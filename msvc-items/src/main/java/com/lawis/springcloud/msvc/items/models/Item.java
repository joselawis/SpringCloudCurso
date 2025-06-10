package com.lawis.springcloud.msvc.items.models;

import com.lawis.libs.msvc.commons.models.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    private Product product;
    private Integer quantity;

    public Double getTotal() {
        return product.getPrice() * quantity;
    }

}

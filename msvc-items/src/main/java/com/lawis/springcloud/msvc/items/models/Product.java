package com.lawis.springcloud.msvc.items.models;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {

    private Long id;
    private String name;
    private Double price;
    private LocalDate createdAt;
    private int port;

}

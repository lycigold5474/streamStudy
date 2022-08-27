package com.example.stream.testcode0.model;

import lombok.Data;

@Data
public class Product {
    private String name;
    private int amount;

    public Product() {
    }

    public Product(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }
}

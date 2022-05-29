package ru.javabegin.BookStore.entity;

import java.util.List;

public class Market {
    private List<books> products;
    public Market(List<books> products) {
        this.products = products;
    }

    public List<books> getProducts() {
        return products;
    }

    public void setProducts(List<books> products) {
        this.products = products;
    }
}

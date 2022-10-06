package com.kai.shoppingcart.model;

public class Item {
    String name;
    double price;
    int discount;
    String uri;

    public Item(String name, double price, int discount, String uri) {
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}

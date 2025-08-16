package org.example.model;

public class CarPrice {

    private String currency;
    private Double price;

    public Double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return String.format("%-1s %-1.2f", getCurrency(), getPrice());
    }
}

package org.example.model;

import org.example.processor.impl.parser.CSVCarBrand;
import org.example.processor.impl.parser.XMLCarType;

import java.time.LocalDate;
import java.util.List;

public class Car {

    private final String brand;
    private final LocalDate releasedDate;
    private final String type;
    private final String model;
    private final CarPrice price;
    private final List<CarPrice> prices;

    public Car(CSVCarBrand brand, XMLCarType carType) {
        this.brand = brand.getBrand();
        this.releasedDate = brand.getReleasedDate();

        this.type = carType.getType();
        this.model = carType.getModel();
        this.price = carType.getDefaultPrice();
        this.prices = carType.getPrices();

    }

    public String getBrand() {
        return this.brand;
    }

    public LocalDate getReleasedDate() {
        return releasedDate;
    }

    public String getType() {
        return this.type;
    }


    public String getModel() {
        return model;
    }


    public CarPrice getPrice() {
        return this.price;
    }


    public List<CarPrice> getPrices() {
        return this.prices;
    }

}

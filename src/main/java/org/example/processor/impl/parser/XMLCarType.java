package org.example.processor.impl.parser;

import org.example.model.CarPrice;

import java.util.ArrayList;
import java.util.List;

public class XMLCarType {

    private String type;
    private String model;
    private CarPrice defaultPrice;
    private final List<CarPrice> prices = new ArrayList<>();

    void setType(String type) {
        this.type = type;
    }

    void setModel(String model) {
        this.model = model;
    }

     void setDefaultPrice(CarPrice defaultPrice) {
        this.defaultPrice = defaultPrice;
    }


    public String getType() {
        return type;
    }


    public String getModel() {
        return model;
    }


    public CarPrice getDefaultPrice() {
        return defaultPrice;
    }

    public List<CarPrice> getPrices() {
        return prices;
    }

}

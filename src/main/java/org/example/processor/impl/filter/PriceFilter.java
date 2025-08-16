package org.example.processor.impl.filter;

import org.example.model.Car;
import org.example.processor.interfaces.CarFilter;

public class PriceFilter implements CarFilter {
    @Override
    public String getField() {
        return "price";
    }

    @Override
    public boolean match(Car car, String value) {
        try {
            return car.getPrice().getPrice() == Double.parseDouble(value);
        } catch (Exception ex) {
            throw new RuntimeException("Invalid price filter value " + value, ex);
        }
    }

    @Override
    public String getDescription() {
        return "Filter by price";
    }

}

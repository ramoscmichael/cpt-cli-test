package org.example.processor.impl.filter;

import org.example.model.Car;
import org.example.processor.interfaces.CarFilter;

public class BrandFilter implements CarFilter {
    @Override
    public String getField() {
        return "brand";
    }

    @Override
    public boolean match(Car car, String value) {
        return car.getBrand().equals(value);
    }

    @Override
    public String getDescription() {
        return "Filter by brand";
    }
}

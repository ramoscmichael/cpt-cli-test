package org.example.processor.interfaces;

import org.example.model.Car;

import java.util.function.Predicate;

public interface CarFilter {
    String getField();
    boolean match(Car car, String value);

    default Predicate<Car> create(String value) {
        return (car) -> this.match(car, value);
    }

    String getDescription();
}

package org.example.processor.interfaces;

import org.example.model.Car;

import java.util.Comparator;

public interface CarSorter {

    String getSorterName();
    int compare(Car car1, Car car2);

    default Comparator<Car> create() {
        return this::compare;
    }

    String getDescription();
}

package org.example.processor.impl.sorter;

import org.example.model.Car;
import org.example.processor.interfaces.CarSorter;

public class PriceSorter implements CarSorter {

    @Override
    public String getSorterName() {
        return "price";
    }

    @Override
    public int compare(Car car1, Car car2) {
        return car1.getPrice().getPrice().compareTo(car2.getPrice().getPrice()) * -1;
    }

    @Override
    public String getDescription() {
        return "Sort price by highest to lowest";
    }
}

package org.example.processor.impl.sorter;

import org.example.model.Car;
import org.example.processor.interfaces.CarSorter;

public class TypeSorter implements CarSorter {
    @Override
    public String getSorterName() {
        return "type";
    }

    @Override
    public int compare(Car car1, Car car2) {
        return car1.getType().compareTo(car2.getType());
    }

    @Override
    public String getDescription() {
        return "Sort by type";
    }
}

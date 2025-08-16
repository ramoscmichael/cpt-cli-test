package org.example.processor.impl.sorter;

import org.example.model.Car;
import org.example.processor.interfaces.CarSorter;

public class ReleaseYearSorter implements CarSorter {

    @Override
    public String getSorterName() {
        return "releaseYear";
    }

    @Override
    public int compare(Car car1, Car car2) {
        return car1.getReleasedDate().compareTo(car2.getReleasedDate()) * -1;
    }


    @Override
    public String getDescription() {
        return "Sort releaseDate by latest to oldest";
    }

}

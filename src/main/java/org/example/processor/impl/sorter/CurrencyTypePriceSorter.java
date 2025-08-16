package org.example.processor.impl.sorter;

import org.example.model.Car;
import org.example.model.CarPrice;
import org.example.processor.interfaces.CarSorter;

import java.util.Objects;

public class CurrencyTypePriceSorter implements CarSorter  {

    @Override
    public String getSorterName() {
        return "typeCurrPrice";
    }

    @Override
    public String getDescription() {
        return """
            Sort car price by type and currency
            SUVs sorted in EUR
            Sedans sorted in JPY
            Trucks sorted in USD
        """;
    }

    @Override
    public int compare(Car car1, Car car2) {

        CarPrice price1 = getPriceByType(car1);
        CarPrice price2 = getPriceByType(car2);

        return price1.getPrice().compareTo(price2.getPrice());
    }

    private CarPrice getPriceByType(Car car) {
        return switch (car.getType()) {
            case "SUV" -> car.getPrices().stream()
                    .filter(p -> Objects.equals(p.getCurrency(), "EUR"))
                    .findFirst()
                    .orElse(car.getPrice());
            case "Sedan" ->
                    car.getPrices().stream()
                    .filter(p -> Objects.equals(p.getCurrency(), "JPY"))
                    .findFirst().orElse(car.getPrice());
            default -> car.getPrice();
        };
    }
}

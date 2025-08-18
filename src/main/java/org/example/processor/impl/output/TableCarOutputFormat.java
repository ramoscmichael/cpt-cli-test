package org.example.processor.impl.output;

import org.example.model.Car;
import org.example.model.CarParam;
import org.example.model.CarPrice;
import org.example.processor.interfaces.CarOutputFormat;

import java.io.PrintWriter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TableCarOutputFormat implements CarOutputFormat {
    @Override
    public String getFormat() {
        return "table";
    }

    @Override
    public void output(Stream<Car> cars, CarParam param, PrintWriter out) {
        out.println("Output:");
        out.printf("%-15s %-15s %-15s %-15s %-15s %-15s%n",
                "Brand",
                "Release Date",
                "Model",
                "Type",
                "Price",
                "Prices");
        out.println(String.valueOf("-").repeat(125));
        cars.forEach(c -> {
            // Data rows
            out.printf("%-15s %-15s %-15s %-15s %-15s %-15s%n",
                    c.getBrand(),
                    c.getReleasedDate(),
                    c.getModel(),
                    c.getType(),
                    c.getPrice().toString(),
                    c.getPrices().stream()
                            .map(CarPrice::toString)
                            .collect(Collectors.joining(", ", "[", "]"))
            );
        });
    }

    @Override
    public String getDescription() {
        return "Show table results in console";
    }
}

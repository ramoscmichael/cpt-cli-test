package org.example.processor.impl.parser;

import java.time.LocalDate;

public class CSVCarBrand {

    private final LocalDate releasedDate;
    private final String brand;

    public CSVCarBrand(String brand, LocalDate releasedDate) {
        this.brand = brand;
        this.releasedDate = releasedDate;
    }

    public LocalDate getReleasedDate() {
        return releasedDate;
    }

    public String getBrand() {
        return brand;
    }

}

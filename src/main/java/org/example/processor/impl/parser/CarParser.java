package org.example.processor.impl.parser;

import org.example.model.Car;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Car parser that accepts CSV iterator and XML iterator based from the source files
 */
public class CarParser implements Iterator<Car>, AutoCloseable {

    private final CSVBrandParser brandIterator;
    private final XMLCarTypeParser carTypeIterator;

    private CarParser(CSVBrandParser brandIterator, XMLCarTypeParser carTypeIterator) {
        this.brandIterator = brandIterator;
        this.carTypeIterator = carTypeIterator;
    }

    @Override
    public boolean hasNext() {
        return this.brandIterator.hasNext(); //&& this.carTypeIterator.hasNext();
    }

    @Override
    public Car next() {
        CSVCarBrand brand = brandIterator.next();
        XMLCarType type = carTypeIterator.next();
        return new Car(brand, type);
    }

    @Override
    public void close() throws Exception {
        this.brandIterator.close();
        this.carTypeIterator.close();
    }

    public static Stream<Car> parse(String csvBrandSrc, String xmlCarTypeSrc) throws Exception {
        Iterable<Car> iterable = () -> new CarParser(new CSVBrandParser(csvBrandSrc), new XMLCarTypeParser(xmlCarTypeSrc));
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}

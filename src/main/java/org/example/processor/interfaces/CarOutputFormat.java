package org.example.processor.interfaces;

import org.example.model.Car;
import org.example.model.CarParam;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public interface CarOutputFormat {
    String getFormat();
    void output(Stream<Car> cars, CarParam param, PrintWriter out);

    default FileWriter getFileWriter(String file) throws Exception {
        Path path = Paths.get(file);
        Files.createDirectories(path.getParent());
        return new FileWriter(file);
    }

    default DateTimeFormatter getDateFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    default Iterable<Car> toIterable(Stream<Car> cars) {
        return cars::iterator;  // lazily pulls from the stream
    }

    String getDescription();
}

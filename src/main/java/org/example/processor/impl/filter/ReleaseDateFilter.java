package org.example.processor.impl.filter;

import org.apache.commons.lang3.StringUtils;
import org.example.model.Car;
import org.example.processor.interfaces.CarFilter;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Pattern;

public class ReleaseDateFilter implements CarFilter {

    private final Map<String, String> regexDateFormats =  Map.of(
            "yyyy-MM-dd", "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$",
            "yyyy,dd,MM", "^\\d{4},(0[1-9]|[12]\\d|3[01]),(0[1-9]|1[0-2])$"
    );

    @Override
    public String getField() {
        return "releaseDate";
    }

    @Override
    public boolean match(Car car, String value) {

        return regexDateFormats.entrySet()
                .stream()
                .filter(p -> Pattern.matches(p.getValue(), value))
                .findFirst()
                .map(p -> DateTimeFormatter.ofPattern(p.getKey()))
                .map(f -> f.format(car.getReleasedDate()).equals(value))
                .orElseThrow(() -> new RuntimeException("Invalid filter releaseDate value " + value));
    }

    @Override
    public String getDescription() {
        return "Filter by releaseDate using allowed format(s) (" + StringUtils.join(regexDateFormats.keySet(), ", ") + ")";
    }
}

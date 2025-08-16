package org.example.processor.impl.output;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.example.model.Car;
import org.example.model.CarParam;
import org.example.processor.interfaces.CarOutputFormat;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class JSONCarOutputFormat implements CarOutputFormat {
    private final DateTimeFormatter dateTimeFormatter = getDateFormatter();

    @Override
    public String getFormat() {
        return "json";
    }

    @Override
    public void output(Stream<Car> cars, CarParam param, PrintWriter out) {

        System.out.println("Output: " + param.getOutOption());
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter(dateTimeFormatter))
                .create();

        try (JsonWriter writer = new JsonWriter(getFileWriter(param.getOutOption()))) {
            writer.setIndent("\t");
            writer.beginArray();

            for (Car car: toIterable(cars)) {
                gson.toJson(car, Car.class, writer);
            }

            writer.endArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static class LocalDateAdapter extends TypeAdapter<LocalDate> {
        private final DateTimeFormatter formatter;
        LocalDateAdapter(DateTimeFormatter formatter) {
            this.formatter = formatter;
        }

        @Override
        public void write(JsonWriter out, LocalDate value) throws IOException {
            out.value(value.format(formatter));
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            return LocalDate.parse(in.nextString(), formatter);
        }
    }

    @Override
    public String getDescription() {
        return "Export result in a json file";
    }
}

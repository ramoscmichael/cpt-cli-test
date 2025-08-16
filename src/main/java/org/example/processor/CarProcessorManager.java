package org.example.processor;

import org.apache.commons.lang3.StringUtils;
import org.example.model.Car;
import org.example.model.CarParam;
import org.example.processor.impl.parser.CarParser;
import org.example.processor.interfaces.CarFilter;
import org.example.processor.interfaces.CarOutputFormat;
import org.example.processor.interfaces.CarSorter;

import java.io.PrintWriter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CarProcessorManager {

    private final Map<String, CarOutputFormat> outputMap;
    private final Map<String, CarFilter> filterMap;
    private final Map<String, CarSorter> sorterMap;

    public List<String> getSorterNames() {
        return sorterMap.keySet().stream().toList();
    }

    public List<String> getFilterFieldNames() {
        return filterMap.keySet().stream().toList();
    }

    public List<String> getFormatNames() {
        return outputMap.keySet().stream().toList();
    }

    public Stream<CarOutputFormat> getFormats() {
        return this.outputMap.values().stream();
    }

    public Stream<CarFilter> getFilters() {
        return this.filterMap.values().stream();
    }

    public Stream<CarSorter> getSorters() {
        return this.sorterMap.values().stream();
    }

    protected static class CarParserFactoryInstance {
        public static CarProcessorManager INSTANCE = new CarProcessorManager();
    }

    public static CarProcessorManager getInstance() {
        return CarParserFactoryInstance.INSTANCE;
    }

    private CarProcessorManager() {

        // Dynamically load output formatter from META-INF/services
        ServiceLoader<CarOutputFormat> outputs = ServiceLoader.load(CarOutputFormat.class);
        this.outputMap = outputs.stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toMap(CarOutputFormat::getFormat, o -> o));

        // Dynamically load filter from META-INF/services
        ServiceLoader<CarFilter> filters = ServiceLoader.load(CarFilter.class);
        this.filterMap = filters.stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toMap(CarFilter::getField, o -> o));

        // Dynamically load sorters from META-INF/services
        ServiceLoader<CarSorter> sorters = ServiceLoader.load(CarSorter.class);
        this.sorterMap = sorters.stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toMap(CarSorter::getSorterName, o -> o));
    }

    /**
     * Validate parameters like filters, sorters and format
     * @param param
     */
    public void validate(CarParam param) {

        CarOutputFormat output = this.outputMap.get(param.getFormatOption());
        if (Objects.isNull(output)) {
            throw new CarParameterException("Invalid format option: '--format' only accepts " + StringUtils.join(getFormatNames(), ","));
        }

        List<String> invalidParams = param.getFilterParams()
                .stream()
                .map(CarParam.FilterParam::getField)
                .filter(f -> !this.getFilterFieldNames().contains(f))
                .toList();

        if (!invalidParams.isEmpty()) {
            throw new CarParameterException("Invalid filter option: '--filter' only accepts keys " + StringUtils.join(getFilterFieldNames(), ","));
        }

        List<String> invalidSorters = param.getSortOptions()
                .stream()
                .filter(s -> !getSorterNames().contains(s))
                .toList();

        if (!invalidSorters.isEmpty()) {
            throw new CarParameterException("Invalid sort option: '--sort' only accepts keys " + StringUtils.join(getSorterNames(), ","));
        }
    }

    /**
     * This will parse the files provided in CarParam and apply the filters and sorters and process to defined format output
     *
     * @param param
     * @param writer
     */
    public void process(CarParam param, PrintWriter writer) {
        Stream<Car> cars = Stream.empty();
        try {
            validate(param);

            CarOutputFormat outputFormat = outputMap.get(param.getFormatOption());

            // create predicate from param filters
            Predicate<Car> filter = param.getFilterParams()
                    .stream()
                    .map(fp -> this.filterMap.get(fp.field).create(fp.value))
                    .reduce(x -> true, Predicate::and);

            // create a comparator from param sorters
            // we follow the sort sequence from the param
            Comparator<Car> sorted = param.getSortOptions()
                    .stream()
                    .map(sorterMap::get)
                    .map(CarSorter::create)
                    .reduce(Comparator::thenComparing)
                    .orElse(null); // provide empty sort

            // Parse source to Stream of Car model
            cars = CarParser.parse(param.getBrandsSrcOption(), param.getCarTypesSrcOption())
                        .filter(filter);

            // apply sorting only if there is provided to avoid loading all objects in heap
            if (!Objects.isNull(sorted)) {
                cars = cars.sorted(sorted);
            }

            outputFormat.output(cars, param, writer);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            try { cars.close(); } catch (Exception ignore) {;}
        }
    }
}

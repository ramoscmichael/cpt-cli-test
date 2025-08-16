package org.example.model;

import org.apache.commons.lang3.StringUtils;
import org.example.processor.CarParameterException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CarParam {

    private String carTypesSrcOption;
    private String brandsSrcOption;
    private String formatOption;
    private String outOption;

    private boolean overwriteOption;
    private final List<String> sortOptions = new ArrayList<>();
    private final List<FilterParam> filterParams = new ArrayList<>();

    public boolean isOverwriteOption() {
        return overwriteOption;
    }

    public String getCarTypesSrcOption() {
        return carTypesSrcOption;
    }

    public String getBrandsSrcOption() {
        return brandsSrcOption;
    }

    public String getFormatOption() {
        return formatOption;
    }

    public String getOutOption() {
        return outOption;
    }

    public List<String> getSortOptions() {
        return sortOptions;
    }

    public List<FilterParam> getFilterParams() {
        return filterParams;
    }

    public static CarParam create() {
        return new CarParam();
    }

    public CarParam overwriteOption(boolean overwriteOption) {
        this.overwriteOption = overwriteOption;
        return this;
    }

    public CarParam carTypesSrcOptions(String  carTypesSrcOptions) {
        Path path = Paths.get(carTypesSrcOptions);
        if (!Files.exists(path)) {
            throw new CarParameterException("Invalid carTypesSrc option: '--carTypesSrc' file does not exists");
        }

        this.carTypesSrcOption = carTypesSrcOptions;
        return this;
    }

    public CarParam brandsSrcOption(String brandsSrcOption) {
        Path path = Paths.get(brandsSrcOption);
        if (!Files.exists(path)) {
            throw new CarParameterException("Invalid brandsSrc option: '--brandsSrc' file does not exists");
        }

        this.brandsSrcOption = brandsSrcOption;
        return this;
    }

    public CarParam formatOption(String formatOption, String outOption) {
        if (!formatOption.matches("json|table|xml")) {
            throw new CarParameterException("Invalid format option: '--format' only accepts table,json or xml");
        }

        if (formatOption.matches("json|xml") && StringUtils.isBlank(outOption)) {
            throw new CarParameterException("Invalid format option: '--format' json or xml must have valid --out option");
        }

        this.formatOption = formatOption;
        return this.outOption(outOption);
    }

    public CarParam outOption(String outOption) {
        if(!this.formatOption.equals("table")) {
            Path path = Paths.get(outOption);
            if (Files.exists(path) && !overwriteOption) {
                throw new CarParameterException("Invalid out option: '--out' file already exists. If you want to overwrite the file add flag  --overwrite");
            }
        }
        this.outOption = outOption;
        return this;
    }

    public CarParam sortOption(String sortOption) {
        this.sortOptions.addAll(Arrays
            .stream(sortOption.split(","))
            .map(String::trim)
            .filter(StringUtils::isNoneBlank)
            .toList()
        );
        return this;
    }

    public CarParam filterOption(String filterOption) {
        Pattern pattern = Pattern.compile("(\\w+)=([^&]+)");
        Matcher matcher = pattern.matcher(filterOption);

        while (matcher.find()) {
            String field = matcher.group(1);
            String value = matcher.group(2);

            getFilterParams().add(new FilterParam(field, value));
        }
        return this;
    }

    public static class FilterParam {

        public String field;
        public String value;

        public FilterParam(String field, String value) {
            this.field = field;
            this.value = value;
        }

        public String getField() {
            return field;
        }

        public String getValue() {
            return value;
        }
    }

}

package org.example.processor.impl.parser;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * CSV parser for Car brands
 */
public class CSVBrandParser implements Iterator<CSVCarBrand>, AutoCloseable {

    private static final String BRAND_HEADER = "Brand";
    private static final String RELEASE_DATE_HEADER = "ReleaseDate";

    private final Map<String, Integer> headerIndex;

    private final BufferedReader reader;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private String nexLine;

    public CSVBrandParser(String file) {
        try {
            this.reader = new BufferedReader(new FileReader(file));
            this.nexLine = reader.readLine();

            if (StringUtils.isBlank(this.nexLine)) {
                throw new RuntimeException("Invalid csv. No header lines");
            }

            String current = this.nexLine.trim().replace("\uFEFF", "");
            String[] headers = current.substring(1, current.length() - 1).split(",");

            headerIndex = IntStream.range(0, headers.length)
                    .boxed()
                    .collect(Collectors.toMap(i -> headers[i], i -> i));

            this.nexLine = reader.readLine();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to properly parse CSV file. Please check if format", ex);
        }

    }

    @Override
    public boolean hasNext() {
        return !Objects.isNull(this.nexLine);
    }

    @Override
    public CSVCarBrand next() {
        try {
            String current = this.nexLine.trim().replace("\uFEFF", "");
            this.nexLine = reader.readLine();

            String[] values = current.trim().substring(1, current.length() -1).split(",");

            return new CSVCarBrand(
                    values[headerIndex.get(BRAND_HEADER)],
                    LocalDate.parse(values[headerIndex.get(RELEASE_DATE_HEADER)], formatter)
            );
        } catch (Exception ex) {
            throw new RuntimeException("Unable to properly parse CSV file. Please make sure the file is a valid csv file for car brands.\n" + ex.getMessage(), ex);
        }
    }

    @Override
    public void close() throws Exception {
        try { this.reader.close(); } catch (Exception ignore) {;}
    }
}

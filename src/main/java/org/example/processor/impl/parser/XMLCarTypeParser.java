package org.example.processor.impl.parser;

import org.example.model.CarPrice;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.util.Iterator;

/**
 * XML parser for Car types
 */
public class XMLCarTypeParser implements Iterator<XMLCarType>, AutoCloseable {

    private static final String TYPE_FIELD = "type";
    private static final String MODEL_FIELD = "model";
    private static final String PRICE_FIELD = "price";
    private static final String PRICES_FIELD = "prices";
    private static final String CURRENCY_FIELD = "currency";
    private static final String CAR_FIELD = "car";

    private final XMLStreamReader reader;
    public XMLCarTypeParser(String file) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            reader = factory.createXMLStreamReader(new FileInputStream(file));
        } catch (Exception ex) {
            throw new RuntimeException("Error in loading xml", ex);
        }
    }

    @Override
    public boolean hasNext() {
        try {
            while (reader.hasNext()) {
                if (reader.next() == XMLStreamConstants.START_ELEMENT &&
                        reader.getLocalName().equals(CAR_FIELD)) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            throw new RuntimeException("Error in loading xml", ex);
        }
    }

    @Override
    public XMLCarType next() {
        XMLCarType carType = new XMLCarType();
        boolean isPrices = false;
        try {
            while (reader.hasNext()) {
                int tag = reader.next();
                if( tag == XMLStreamConstants.END_ELEMENT && reader.getLocalName().equals(CAR_FIELD)){
                    return carType;
                }

                if (tag == XMLStreamConstants.START_ELEMENT) {
                    if (reader.getLocalName().equals(TYPE_FIELD)) {
                        reader.next(); // point to CHARACTER
                        carType.setType(reader.getText().trim());
                        continue;
                    }

                    if (reader.getLocalName().equals(MODEL_FIELD)) {
                        reader.next(); // point to CHARACTER
                        carType.setModel(reader.getText().trim());
                        continue;
                    }

                    if (reader.getLocalName().equals(PRICE_FIELD)) {
                        String currency = reader.getAttributeValue(null, CURRENCY_FIELD);

                        reader.next(); // point to CHARACTER
                        Double price = Double.valueOf(reader.getText().trim());

                        CarPrice carPrice = new CarPrice();
                        carPrice.setCurrency(currency);
                        carPrice.setPrice(price);

                        if(!isPrices) {
                            carType.setDefaultPrice(carPrice);
                        } else {
                            carType.getPrices().add(carPrice);
                        }

                        continue;
                    }

                    if (reader.getLocalName().equals(PRICES_FIELD)) {
                        isPrices = true;
                    }
                }

                else if (tag == XMLStreamConstants.END_ELEMENT && reader.getLocalName().equals(PRICES_FIELD)) {
                    isPrices = false;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Unable to properly parse XML file. Please make sure the file is a valid xml file for car types." + ex.getMessage(), ex);
        }

        return carType;
    }


    @Override
    public void close() throws Exception {
        try { this.reader.close(); } catch (Exception ignore) {;}
    }
}

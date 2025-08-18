package org.example.processor.impl.output;

import org.example.model.Car;
import org.example.model.CarParam;
import org.example.model.CarPrice;
import org.example.processor.interfaces.CarOutputFormat;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Stream;

public class XMLCarOutputFormat implements CarOutputFormat {

    private final DateTimeFormatter dateTimeFormatter = getDateFormatter();

    @Override
    public String getFormat() {
        return "xml";
    }

    private void addNewLineIndention(Writer writer, int indentionCount) throws IOException{
        writer.write("\n");
        writer.write(String.valueOf("\t").repeat(indentionCount));
    }

    @Override
    public void output(Stream<Car> cars, CarParam param, PrintWriter out) {
        out.println("Output: " + param.getOutOption());
        try (FileWriter fileWriter = getFileWriter(param.getOutOption())) {
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = factory.createXMLStreamWriter(fileWriter);
            writer.writeStartDocument();

            addNewLineIndention(fileWriter, 0);
            writer.writeStartElement("cars");

            for(Car c : toIterable(cars)) {
                addNewLineIndention(fileWriter, 2);
                writer.writeStartElement("car");

                addNewLineIndention(fileWriter, 2);
                writeElementValue(writer, "brand", c.getBrand());

                addNewLineIndention(fileWriter, 2);
                writeElementValue(writer, "releaseDate", dateTimeFormatter.format(c.getReleasedDate()));

                addNewLineIndention(fileWriter, 2);
                writeElementValue(writer, "type", c.getType());

                addNewLineIndention(fileWriter, 2);
                writeElementValue(writer, "model", c.getModel());

                addNewLineIndention(fileWriter, 2);
                writeElementValue(writer, "price", c.getPrice().getPrice().toString(), "currency", c.getPrice().getCurrency());

                addNewLineIndention(fileWriter, 2);
                writer.writeStartElement("prices");
                for (CarPrice price: c.getPrices()) {
                    addNewLineIndention(fileWriter, 3);
                    writeElementValue(writer, "price", price.getPrice().toString(), "currency", price.getCurrency());
                }
                addNewLineIndention(fileWriter, 2);
                writer.writeEndElement(); // prices

                addNewLineIndention(fileWriter, 1);
                writer.writeEndElement(); // car
            }
            addNewLineIndention(fileWriter, 1);
            writer.writeEndElement(); //cars
            addNewLineIndention(fileWriter, 0);
            writer.writeEndDocument();
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }

    private void writeElementValue(XMLStreamWriter writer, String name, String value) throws XMLStreamException {
        writeElementValue(writer, name, value, null, null);
    }

    private void writeElementValue(XMLStreamWriter writer, String name, String value, String attributeKey, String attributeValue) throws XMLStreamException {
        writer.writeStartElement(name);

        if (!Objects.isNull(attributeKey) && !Objects.isNull(attributeValue)) {
            writer.writeAttribute(attributeKey, attributeValue);
        }

        if (!Objects.isNull(value)) {
            writer.writeCharacters(value);
        }

        writer.writeEndElement();
    }

    @Override
    public String getDescription() {
        return "Export result in a xml file";
    }

}

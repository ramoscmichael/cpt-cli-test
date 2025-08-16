package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest
{

    @Test
    public void testHelpOption() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        App app = new App(new PrintWriter(out));

        int exitCode = app.execute("-h");
        System.out.println(out.toString());

        assertEquals(0, exitCode);
        assertTrue(out.toString().contains("Usage:"), "Help output should contain Usage");
    }

    @Test
    void testNoOptions() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        App app = new App(new PrintWriter(out));

        int exitCode = app.execute();
        assertEquals(2, exitCode);
    }

    @Test
    void testTableOutput() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        App app = new App(new PrintWriter(out));

        int exitCode = app.execute(
                "--brandsSrc", "./data/CarsBrand.csv",
                "--carTypesSrc", "./data/carsType.xml"
        );

        assertEquals(0, exitCode);
    }


    @Test
    void testJsonOutput() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        App app = new App(new PrintWriter(out));

        int exitCode = app.execute(
                "--brandsSrc", "./data/CarsBrand.csv",
                "--carTypesSrc", "./data/carsType.xml",
                "--filter", "brand=&releaseDate=2023-01-15",
                "--sort", "type,typeCurrPrice",
                "--format", "json",
                "--out", "./data/output/cars.json",
                "--overwrite"
        );

        assertEquals(0, exitCode);
    }

    @Test
    void testXmlOutput() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        App app = new App(new PrintWriter(out));

        int exitCode = app.execute(
                "--brandsSrc", "./data/CarsBrand.csv",
                "--carTypesSrc", "./data/carsType.xml",
                "--filter", "brand=&releaseDate=2023-01-15",
                "--sort", "type,typeCurrPrice",
                "--format", "xml",
                "--out", "./data/output/cars.xml",
                "--overwrite"
        );

        assertEquals(0, exitCode);
    }


}

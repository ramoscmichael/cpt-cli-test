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
        App app = new App(new PrintWriter(out, true));

        int exitCode = app.execute("-h");
        System.out.println(out.toString());

        assertEquals(0, exitCode);
        assertTrue(out.toString().contains("Usage:"), "Help output should contain Usage");
    }

    @Test
    void testNoOptions() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        App app = new App(new PrintWriter(out, true));

        int exitCode = app.execute();

        System.out.println(out.toString());
        assertEquals(2, exitCode);
    }

    @Test
    void testTableOutput() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        App app = new App(new PrintWriter(System.out, true));

        int exitCode = app.execute(
                "--brandsSrc", "./data/CarsBrand.csv",
                "--carTypesSrc", "./data/carsType.xml"
        );

        System.out.println(out.toString());
        assertEquals(0, exitCode);
    }


    @Test
    void testJsonOutput() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        App app = new App(new PrintWriter(out, true));

        int exitCode = app.execute(
                "--brandsSrc", "./data/CarsBrand.csv",
                "--carTypesSrc", "./data/carsType.xml",
                "--filter", "brand=&releaseDate=2023-01-15",
                "--sort", "type,typeCurrPrice",
                "--format", "json",
                "--out", "./data/output/cars.json",
                "--overwrite"
        );

        System.out.println(out.toString());
        assertEquals(0, exitCode);
    }

    @Test
    void testXmlOutput() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        App app = new App(new PrintWriter(out, true));

        int exitCode = app.execute(
                "--brandsSrc", "./data/CarsBrand.csv",
                "--carTypesSrc", "./data/carsType.xml",
                "--filter", "brand=&releaseDate=2023-01-15",
                "--sort", "type,typeCurrPrice",
                "--format", "xml",
                "--out", "./data/output/cars.xml",
                "--overwrite"
        );

        System.out.println(out.toString());
        assertEquals(0, exitCode);
    }


}

# CPT-CLI

Command Line Application for Parsing and Processing CAR (XML) and CSV Files

---

## 🚀 Build Instructions

This project uses **Maven**.

### 1. Clean & Build
Run the following command in the project root:

```bash
mvn clean package
```

This will:
- Clean previous builds
- Compile your sources
- Package the CLI into a runnable JAR (with dependencies shaded inside)

The final JAR will be located in:

```
target/cpt-cli-1.0-SNAPSHOT.jar
```
---

## ▶️ Running the CLI

Run the JAR with:

```bash
java -jar target/cpt-cli-1.0-SNAPSHOT.jar [OPTIONS]
```

### Examples

Show help:

```bash

java -jar target/cpt-cli-1.0-SNAPSHOT.jar -h
Usage: Car Parser [-hV] [--overwrite] --brandsSrc=XML_FILE
                  --carTypesSrc=CSV_FILE [--filter=PARAM] [--format=PARAM]
                  [--out=OUTPUT_FILE] [--sort=PARAM]
This app will parse and process Car data based on the sources provided
      --brandsSrc=XML_FILE   CSV file for car brands
      --carTypesSrc=CSV_FILE XML file for car types
      --filter=PARAM         Filter result by allowed field(s)
                             List of available fields
                             releaseDate     -  Filter by releaseDate using
                               allowed format(s) (yyyy,dd,MM, yyyy-MM-dd)
                             price           -  Filter by price
                             brand           -  Filter by brand
      --format=PARAM         Output format default is table
                             List of available formats
                             xml             -  Export result in a xml file
                             json            -  Export result in a json file
                             table           -  Show table results in console
  -h, --help                 Show this help message and exit.
      --out=OUTPUT_FILE      Output path. This is required for json/xml format
      --overwrite            Overwrite output file if exists.
      --sort=PARAM           Sort the results by provided sorter
                             It will allow multiple sorter using comma ','.
                             Ex: --sort type,price
                             List of available sorters
                             typeCurrPrice   -  Sort car price by type and
                               currency
                                 SUVs sorted in EUR
                                 Sedans sorted in JPY
                                 Trucks sorted in USD
                             price           -  Sort price by highest to lowest
                             type            -  Sort by type
                             releaseYear     -  Sort releaseDate by latest to
                               oldest
  -V, --version              Print version information and exit.
```

Run a command by providing the required params brandsSrc and carTypesSrc

```bash
java -jar target/cpt-cli-1.0-SNAPSHOT.jar --brandsSrc brands.csv --carTypesSrc carTypes.xml -filter brand=Toyota&releaseDate=2023-01-15
Output:
Brand           Release Date    Model           Type            Price           Prices         
-----------------------------------------------------------------------------------------------------------------------------
Toyota          2023-01-15      RAV4            SUV             USD 25000.00    [EUR 23000.00, GBP 20000.00, JPY 2800000.00]
```

Another example with options:

```bash
java -jar target/cpt-cli-1.0-SNAPSHOT.jar --brandsSrc brands.csv --carTypesSrc carTypes.xml --format json --out cars.json
Output: cars.json
```

```bash
java -jar target/cpt-cli-1.0-SNAPSHOT.jar --brandsSrc brands.csv --carTypesSrc carTypes.xml  --format xml --sort type,price --filter brand=Honda&releaseDate=2025-01-01 --out cars.xml
Output: cars.xml
```

---

## ⚙️ Development

To recompile after making changes:

```bash
mvn clean package
```

To skip tests during build:

```bash
mvn clean package -DskipTests
```

---

## 📖 Notes

- Requires **Java 17+**
- All dependencies (Picocli, Gson, Commons Lang) are shaded into the JAR
- You can add new filters sorters and formatters by ServiceLoader please see example in META-INF/services

---

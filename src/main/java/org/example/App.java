package org.example;

import org.apache.commons.lang3.StringUtils;
import org.example.model.CarParam;
import org.example.processor.CarParameterException;
import org.example.processor.CarProcessorManager;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.Stream;

@CommandLine.Command(name = "Car Parser",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "This app will parse and process Car data based on the sources provided")
public final class App implements Runnable {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(names = "--carTypesSrc",
            paramLabel = "CSV_FILE",
            description = "XML file for car types",
            required = true)
    private String carTypesSrc;

    @CommandLine.Option(names = "--brandsSrc",
            paramLabel = "XML_FILE",
            description = "CSV file for car brands",
            required = true)
    private String brandsSrc;

    @CommandLine.Option(names = "--out",
            paramLabel = "OUTPUT_FILE",
            description = "Output path. This is required for json/xml format",
            required = false)
    private String outOption;

    @CommandLine.Option(names = "--overwrite",
            paramLabel = "PARAM",
            description = "Overwrite output file if exists.",
            required = false)
    private boolean overwriteOption;

    private final CommandLine cmd;

    public App(PrintWriter writer) {
        this.cmd = new CommandLine(this);
        this.cmd.setOut(writer);

        // add sort option
        this.cmd.getCommandSpec()
                .addOption(formatOptionSpec())
                .addOption(sortOptionSpec())
                .addOption(filterOptionSpec());
    }

    public CommandLine.Model.OptionSpec formatOptionSpec() {
        Stream<String> mainDescription = Arrays.stream(new String[] {
                "Output format default is table",
                "List of available formats"
        });

        Stream<String> detailDescription = CarProcessorManager.getInstance().getFormats()
                .map(f -> String.format("%-15s %-2s %-5s", f.getFormat(), "-", f.getDescription().trim()));

        return CommandLine.Model.OptionSpec
                .builder("--format")
                .type(String.class)
                .description(
                        Stream.concat(mainDescription, detailDescription)
                                .toList()
                                .toArray(new String[]{})
                )
                .defaultValue("table")
                .required(false)
                .build();
    }

    public CommandLine.Model.OptionSpec sortOptionSpec() {
        Stream<String> mainDescription = Arrays.stream(new String[] {
                "Sort the results by provided sorter",
                "It will allow multiple sorter using comma ','.",
                "Ex: --sort type,price",
                "List of available sorters"
        });

        Stream<String> detailDescription = CarProcessorManager.getInstance().getSorters()
                .map(f -> String.format("%-15s %-2s %-5s", f.getSorterName(), "-", f.getDescription().trim()));

        return CommandLine.Model.OptionSpec
                .builder( "--sort")
                .required(false)
                .defaultValue("")
                .description(
                        Stream.concat(mainDescription, detailDescription)
                                .toList()
                                .toArray(new String[]{})
                )
                .type(String.class)
                .build();
    }

    public CommandLine.Model.OptionSpec filterOptionSpec() {
        Stream<String> mainDescription = Arrays.stream(new String[] {
                "Filter result by allowed field(s)",
                "List of available fields"
        });

        Stream<String> detailDescription = CarProcessorManager.getInstance().getFilters()
                .map(f -> String.format("%-15s %-2s %-5s", f.getField(), "-", f.getDescription().trim()));

        return CommandLine.Model.OptionSpec
                .builder("--filter")
                .type(String.class)
                .description(
                        Stream.concat(mainDescription, detailDescription)
                                .toList()
                                .toArray(new String[]{})
                )
                .defaultValue("")
                .required(false)
                .build();
    }


    int execute(String ...args) {
        return this.cmd.execute(args);
    }

    @Override
    public void run() {
        try {

            CommandLine.Model.OptionSpec sortOption = this.cmd.getCommandSpec().findOption("--sort");
            CommandLine.Model.OptionSpec formatOption = this.cmd.getCommandSpec().findOption("--format");
            CommandLine.Model.OptionSpec filterOption = this.cmd.getCommandSpec().findOption("--filter");

            CarParam param = CarParam.create()
                    .carTypesSrcOptions(StringUtils.defaultIfBlank(carTypesSrc, ""))
                    .brandsSrcOption(StringUtils.defaultIfBlank(brandsSrc, ""))
                    .overwriteOption(overwriteOption)
                    .formatOption(StringUtils.defaultIfBlank(formatOption.getValue().toString(), "table"), StringUtils.defaultIfBlank(outOption, ""))
                    .sortOption(StringUtils.defaultIfBlank(sortOption.getValue().toString(), ""))
                    .filterOption(StringUtils.defaultIfBlank(filterOption.getValue().toString(), ""));

            CarProcessorManager
                    .getInstance()
                    .process(param, spec.commandLine().getOut());

        } catch (CarParameterException ex) {
            throw new CommandLine.ParameterException(spec.commandLine(), ex.getMessage());
        } catch (RuntimeException runtimeException) {
            throw new CommandLine.PicocliException(runtimeException.getMessage(), runtimeException.getCause());
        }
    }


    public static void main(String[] args )
    {
        App app = new App(new PrintWriter(System.out));
        app.execute(args);
    }
}

package com.example.traffic.cli;

import com.example.traffic.cli.json.CommandMapper;
import com.example.traffic.cli.json.SimulationInputDto;
import com.example.traffic.core.command.Command;
import com.example.traffic.core.simulation.SimulationEngine;
import com.example.traffic.core.simulation.SimulationOutput;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.nio.file.Path;
import java.util.List;

public final class CliApplication {
    private CliApplication() {
    }

    public static void main(String[] args) {
        int exitCode = run(args);
        System.exit(exitCode);
    }

    static int run(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: traffic-simulator <input.json> <output.json>");
            return 1;
        }

        Path inputPath = Path.of(args[0]);
        Path outputPath = Path.of(args[1]);
        ObjectMapper objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT);

        try {
            SimulationInputDto input = objectMapper.readValue(inputPath.toFile(), SimulationInputDto.class);
            List<Command> commands = CommandMapper.toCommands(input);
            SimulationEngine engine = new SimulationEngine();
            SimulationOutput output = engine.run(commands);
            objectMapper.writeValue(outputPath.toFile(), output);
            return 0;
        } catch (Exception exception) {
            System.err.println("Simulation failed: " + exception.getMessage());
            return 2;
        }
    }
}

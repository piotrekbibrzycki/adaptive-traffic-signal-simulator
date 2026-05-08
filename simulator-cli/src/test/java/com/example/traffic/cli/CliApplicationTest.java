package com.example.traffic.cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class CliApplicationTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void readsInputFileAndWritesRequiredOutputJson(@TempDir Path tempDir) throws Exception {
        Path inputPath = tempDir.resolve("input.json");
        Path outputPath = tempDir.resolve("output.json");

        Files.writeString(inputPath, """
                {
                  "commands": [
                    {
                      "type": "addVehicle",
                      "vehicleId": "vehicle1",
                      "startRoad": "south",
                      "endRoad": "north"
                    },
                    {
                      "type": "addVehicle",
                      "vehicleId": "vehicle2",
                      "startRoad": "north",
                      "endRoad": "south"
                    },
                    {
                      "type": "step"
                    },
                    {
                      "type": "step"
                    },
                    {
                      "type": "addVehicle",
                      "vehicleId": "vehicle3",
                      "startRoad": "west",
                      "endRoad": "south"
                    },
                    {
                      "type": "addVehicle",
                      "vehicleId": "vehicle4",
                      "startRoad": "west",
                      "endRoad": "south"
                    },
                    {
                      "type": "step"
                    },
                    {
                      "type": "step"
                    }
                  ]
                }
                """);

        int exitCode = CliApplication.run(new String[]{
                inputPath.toString(),
                outputPath.toString()
        });

        assertThat(exitCode).isZero();
        assertThat(outputPath).exists();

        JsonNode actualJson = objectMapper.readTree(outputPath.toFile());
        JsonNode expectedJson = objectMapper.readTree("""
                {
                  "stepStatuses": [
                    {
                      "leftVehicles": [
                        "vehicle1",
                        "vehicle2"
                      ]
                    },
                    {
                      "leftVehicles": []
                    },
                    {
                      "leftVehicles": [
                        "vehicle3"
                      ]
                    },
                    {
                      "leftVehicles": [
                        "vehicle4"
                      ]
                    }
                  ]
                }
                """);

        assertThat(actualJson).isEqualTo(expectedJson);
    }

    @Test
    void returnsErrorWhenArgumentsAreMissing() {
        int exitCode = CliApplication.run(new String[]{});

        assertThat(exitCode).isEqualTo(1);
    }
}

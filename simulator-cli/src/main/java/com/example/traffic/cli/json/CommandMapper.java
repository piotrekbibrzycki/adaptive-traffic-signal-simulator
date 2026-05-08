package com.example.traffic.cli.json;

import com.example.traffic.core.command.AddVehicleCommand;
import com.example.traffic.core.command.Command;
import com.example.traffic.core.command.StepCommand;
import com.example.traffic.core.exception.SimulationException;
import com.example.traffic.core.model.Road;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class CommandMapper {
    private CommandMapper() {
    }

    public static List<Command> toCommands(SimulationInputDto input) {
        if (input == null) {
            throw new SimulationException("input must not be null");
        }

        if (input.commands() == null) {
            throw new SimulationException("commands must not be null");
        }

        List<Command> commands = new ArrayList<>();

        for (CommandDto commandDto : input.commands()) {
            commands.add(toCommand(commandDto));
        }

        return List.copyOf(commands);
    }

    private static Command toCommand(CommandDto commandDto) {
        if (commandDto == null) {
            throw new SimulationException("command must not be null");
        }

        if (commandDto.type() == null || commandDto.type().isBlank()) {
            throw new SimulationException("command type must not be null or blank");
        }

        String type = commandDto.type().trim().toLowerCase(Locale.ROOT);

        return switch (type) {
            case "addvehicle" -> toAddVehicleCommand(commandDto);
            case "step" -> new StepCommand();
            default -> throw new SimulationException("unsupported command type: " + commandDto.type());
        };
    }

    private static AddVehicleCommand toAddVehicleCommand(CommandDto commandDto) {
        if (commandDto.vehicleId() == null || commandDto.vehicleId().isBlank()) {
            throw new SimulationException("vehicleId must not be null or blank");
        }

        Road startRoad = parseRoad("startRoad", commandDto.startRoad());
        Road endRoad = parseRoad("endRoad", commandDto.endRoad());

        return new AddVehicleCommand(
                commandDto.vehicleId(),
                startRoad,
                endRoad,
                null
        );
    }

    private static Road parseRoad(String fieldName, String value) {
        try {
            return Road.fromJsonName(value);
        } catch (IllegalArgumentException exception) {
            throw new SimulationException(fieldName + " is invalid: " + value, exception);
        }
    }
}

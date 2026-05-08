package com.example.traffic.core.simulation;

import com.example.traffic.core.command.AddVehicleCommand;
import com.example.traffic.core.command.Command;
import com.example.traffic.core.command.StepCommand;
import com.example.traffic.core.exception.SimulationException;
import com.example.traffic.core.model.Road;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SimulationEngineTest {
    private final SimulationEngine engine = new SimulationEngine();

    @Test
    void createsOneStepStatusForEachStepCommand() {
        SimulationOutput output = engine.run(List.of(
                new StepCommand(),
                new StepCommand(),
                new StepCommand()
        ));

        assertThat(output.stepStatuses()).hasSize(3);
    }

    @Test
    void vehicleCanLeaveIntersection() {
        SimulationOutput output = engine.run(List.of(
                new AddVehicleCommand("vehicle1", Road.SOUTH, Road.NORTH, null),
                new StepCommand()
        ));

        assertThat(output.stepStatuses())
                .extracting(StepStatus::leftVehicles)
                .containsExactly(List.of("vehicle1"));
    }

    @Test
    void matchesRecruitmentTaskSampleOutputAtCoreLevel() {
        List<Command> commands = List.of(
                new AddVehicleCommand("vehicle1", Road.SOUTH, Road.NORTH, null),
                new AddVehicleCommand("vehicle2", Road.NORTH, Road.SOUTH, null),
                new StepCommand(),
                new StepCommand(),
                new AddVehicleCommand("vehicle3", Road.WEST, Road.SOUTH, null),
                new AddVehicleCommand("vehicle4", Road.WEST, Road.SOUTH, null),
                new StepCommand(),
                new StepCommand()
        );

        SimulationOutput output = engine.run(commands);

        assertThat(output.stepStatuses())
                .extracting(StepStatus::leftVehicles)
                .containsExactly(
                        List.of("vehicle1", "vehicle2"),
                        List.of(),
                        List.of("vehicle3"),
                        List.of("vehicle4")
                );
    }

    @Test
    void preservesFifoWithinSameLane() {
        List<Command> commands = List.of(
                new AddVehicleCommand("vehicle1", Road.WEST, Road.SOUTH, null),
                new AddVehicleCommand("vehicle2", Road.WEST, Road.SOUTH, null),
                new StepCommand(),
                new StepCommand()
        );

        SimulationOutput output = engine.run(commands);

        assertThat(output.stepStatuses())
                .extracting(StepStatus::leftVehicles)
                .containsExactly(
                        List.of("vehicle1"),
                        List.of("vehicle2")
                );
    }

    @Test
    void allowsOppositeStraightVehiclesToLeaveInSameStep() {
        List<Command> commands = List.of(
                new AddVehicleCommand("vehicle1", Road.NORTH, Road.SOUTH, null),
                new AddVehicleCommand("vehicle2", Road.SOUTH, Road.NORTH, null),
                new StepCommand()
        );

        SimulationOutput output = engine.run(commands);

        assertThat(output.stepStatuses().getFirst().leftVehicles())
                .containsExactly("vehicle1", "vehicle2");
    }

    @Test
    void rejectsDuplicateVehicleId() {
        List<Command> commands = List.of(
                new AddVehicleCommand("vehicle1", Road.NORTH, Road.SOUTH, null),
                new AddVehicleCommand("vehicle1", Road.SOUTH, Road.NORTH, null)
        );

        assertThatThrownBy(() -> engine.run(commands))
                .isInstanceOf(SimulationException.class)
                .hasMessageContaining("duplicate vehicle id");
    }
}

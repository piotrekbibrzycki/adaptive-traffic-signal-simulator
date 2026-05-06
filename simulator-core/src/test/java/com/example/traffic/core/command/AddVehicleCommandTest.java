package com.example.traffic.core.command;

import com.example.traffic.core.model.Road;
import com.example.traffic.core.model.VehiclePriority;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddVehicleCommandTest {
    @Test
    void defaultsNullPriorityToNormal() {
        AddVehicleCommand command = new AddVehicleCommand("vehicle1", Road.SOUTH, Road.NORTH, null);

        assertThat(command.priority()).isEqualTo(VehiclePriority.NORMAL);
    }

    @Test
    void keepsEmergencyPriority() {
        AddVehicleCommand command = new AddVehicleCommand(
                "ambulance1",
                Road.WEST,
                Road.EAST,
                VehiclePriority.EMERGENCY
        );

        assertThat(command.priority()).isEqualTo(VehiclePriority.EMERGENCY);
    }

    @Test
    void rejectsBlankVehicleId() {
        assertThatThrownBy(() -> new AddVehicleCommand(" ", Road.SOUTH, Road.NORTH, VehiclePriority.NORMAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("vehicle id");
    }

    @Test
    void rejectsSameStartAndEndRoad() {
        assertThatThrownBy(() -> new AddVehicleCommand("vehicle1", Road.SOUTH, Road.SOUTH, VehiclePriority.NORMAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("different");
    }
}

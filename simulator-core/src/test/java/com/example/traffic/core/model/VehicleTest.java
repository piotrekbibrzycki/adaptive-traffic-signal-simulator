package com.example.traffic.core.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VehicleTest {
    @Test
    void defaultsNullPriorityToNormal() {
        Vehicle vehicle = new Vehicle("vehicle1", Road.SOUTH, Road.NORTH, 0, null);

        assertThat(vehicle.priority()).isEqualTo(VehiclePriority.NORMAL);
    }

    @Test
    void keepsEmergencyPriority() {
        Vehicle vehicle = new Vehicle("ambulance1", Road.WEST, Road.EAST, 4, VehiclePriority.EMERGENCY);

        assertThat(vehicle.priority()).isEqualTo(VehiclePriority.EMERGENCY);
    }

    @Test
    void rejectsBlankId() {
        assertThatThrownBy(() -> new Vehicle(" ", Road.SOUTH, Road.NORTH, 0, VehiclePriority.NORMAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Vehicle id");
    }

    @Test
    void rejectsSameStartAndEndRoad() {
        assertThatThrownBy(() -> new Vehicle("vehicle1", Road.SOUTH, Road.SOUTH, 0, VehiclePriority.NORMAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("different");
    }
}

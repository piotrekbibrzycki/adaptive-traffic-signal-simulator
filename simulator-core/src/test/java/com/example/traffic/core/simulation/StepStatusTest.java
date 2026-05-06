package com.example.traffic.core.simulation;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StepStatusTest {
    @Test
    void copiesLeftVehiclesList() {
        List<String> leftVehicles = new ArrayList<>();
        leftVehicles.add("vehicle1");

        StepStatus status = new StepStatus(leftVehicles);

        leftVehicles.add("vehicle2");

        assertThat(status.leftVehicles()).containsExactly("vehicle1");
    }

    @Test
    void rejectsNullLeftVehiclesList() {
        assertThatThrownBy(() -> new StepStatus(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("left vehicles");
    }
}

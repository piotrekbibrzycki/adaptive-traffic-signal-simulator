package com.example.traffic.core.simulation;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SimulationOutputTest {
    @Test
    void copiesStepStatusesList() {
        List<StepStatus> statuses = new ArrayList<>();
        statuses.add(new StepStatus(List.of("vehicle1")));

        SimulationOutput output = new SimulationOutput(statuses);

        statuses.add(new StepStatus(List.of("vehicle2")));

        assertThat(output.stepStatuses()).hasSize(1);
        assertThat(output.stepStatuses().get(0).leftVehicles()).containsExactly("vehicle1");
    }

    @Test
    void rejectsNullStepStatusesList() {
        assertThatThrownBy(() -> new SimulationOutput(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("step statuses");
    }
}

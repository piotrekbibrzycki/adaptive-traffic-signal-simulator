package com.example.traffic.core.simulation;

import java.util.List;

public record SimulationOutput(List<StepStatus> stepStatuses) {
    public SimulationOutput {
        if (stepStatuses == null) {
            throw new IllegalArgumentException("step statuses must not be null");
        }

        stepStatuses = List.copyOf(stepStatuses);
    }
}

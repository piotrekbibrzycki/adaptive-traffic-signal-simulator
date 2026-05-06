package com.example.traffic.core.simulation;

import java.util.List;

public record StepStatus(List<String> leftVehicles) {
    public StepStatus {
        if (leftVehicles == null) {
            throw new IllegalArgumentException("left vehicles must not be null");
        }

        leftVehicles = List.copyOf(leftVehicles);
    }
}

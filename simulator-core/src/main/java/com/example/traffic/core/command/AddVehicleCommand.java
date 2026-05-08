package com.example.traffic.core.command;

import com.example.traffic.core.model.Road;
import com.example.traffic.core.model.VehiclePriority;

public record AddVehicleCommand(
        String vehicleId,
        Road startRoad,
        Road endRoad,
        VehiclePriority priority
) implements Command {
    public AddVehicleCommand {
        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("vehicle id cannot be null or blank");
        }

        if (startRoad == null) {
            throw new IllegalArgumentException("start cannot be null");
        }

        if (endRoad == null) {
            throw new IllegalArgumentException("end road cannot be null");
        }

        if (startRoad == endRoad) {
            throw new IllegalArgumentException("start road and end road must be different");
        }

        priority = priority == null ? VehiclePriority.NORMAL : priority;
    }
}

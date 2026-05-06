package com.example.traffic.core.model;

public record Vehicle(
        String id,
        Road startRoad,
        Road endRoad,
        int createdAtStep,
        VehiclePriority priority
) {
    public Vehicle {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Vehicle id must not be null or blank");
        }

        if (startRoad == null) {
            throw new IllegalArgumentException("start road must not be null");
        }

        if (endRoad == null) {
            throw new IllegalArgumentException("end road must not be null");
        }

        if (startRoad == endRoad) {
            throw new IllegalArgumentException("Start road and end road must be different");
        }

        priority = priority == null ? VehiclePriority.NORMAL : priority;
    }
}

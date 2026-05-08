package com.example.traffic.core.model;

import java.util.Locale;

public enum VehiclePriority {
    NORMAL,
    EMERGENCY;

    public static VehiclePriority fromJsonName(String value) {
        if (value == null || value.isBlank()) {
            return NORMAL;
        }

        return switch (value.trim().toLowerCase(Locale.ROOT)) {
            case "normal" -> NORMAL;
            case "emergency" -> EMERGENCY;
            default -> throw new IllegalArgumentException("unknown vehicle priority: " + value);
        };
    }

    public String toJsonName() {
        return name().toLowerCase(Locale.ROOT);
    }
}

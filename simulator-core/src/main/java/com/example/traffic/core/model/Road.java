package com.example.traffic.core.model;

import java.util.Locale;

public enum Road {
    NORTH,
    SOUTH,
    EAST,
    WEST;

    public Road opposite() {
        return switch(this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }

    public Road left() {
        return switch (this) {
            case NORTH -> EAST;
            case SOUTH -> WEST;
            case EAST -> SOUTH;
            case WEST -> NORTH;
        };
    }

    public Road right() {
        return switch (this) {
            case NORTH -> WEST;
            case SOUTH -> EAST;
            case EAST -> NORTH;
            case WEST -> SOUTH;
        };
        }

    public static Road fromJsonName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("road cannot be null or blank");
        }

        return switch (value.trim().toLowerCase(Locale.ROOT)) {
            case "north" -> NORTH;
            case "south" -> SOUTH;
            case "east" -> EAST;
            case "west" -> WEST;
            default -> throw new IllegalArgumentException("unknown road: " + value);
        };
    }

    public String toJsonName() {
        return name().toLowerCase(Locale.ROOT);
    }
    }


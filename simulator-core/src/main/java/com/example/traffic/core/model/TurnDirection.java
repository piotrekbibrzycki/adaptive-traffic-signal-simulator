package com.example.traffic.core.model;

public enum TurnDirection {
    LEFT,
    STRAIGHT,
    RIGHT;

    public static TurnDirection fromRoads(Road from, Road to) {
        if (from == null) {
            throw new IllegalArgumentException("From road cannot be null");
        }

        if (to == null) {
            throw new IllegalArgumentException("to road must not be null");
        }

        if (from == to) {
            throw new IllegalArgumentException("Start road and end road must be different");
        }

        if (to == from.opposite()) {
            return STRAIGHT;
        }

        if (to == from.left()) {
            return LEFT;
        }

        if (to == from.right()) {
            return RIGHT;
        }

        throw new IllegalArgumentException("Unsupported movement from " + from + " to " + to);
    }
}

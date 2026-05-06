package com.example.traffic.core.model;

public record Movement(Road from, Road to) {
    public Movement {
        if (from == null) {
            throw new IllegalArgumentException("From road must not be null");
        }

        if (to == null) {
            throw new IllegalArgumentException("To road must not be null");
        }

        if (from == to) {
            throw new IllegalArgumentException("Movement must start and end on different roads");
        }
    }

    public TurnDirection turnDirection() {
        return TurnDirection.fromRoads(from, to);
    }

    public LaneType requiredLane() {
        return switch (turnDirection()) {
            case LEFT -> LaneType.LEFT;
            case STRAIGHT -> LaneType.STRAIGHT;
            case RIGHT -> LaneType.RIGHT;
        };
    }
}

package com.example.traffic.core.model;

public record Movement(Road from, Road to) {
    public Movement {
        if (from == null) {
            throw new IllegalArgumentException("from road must not be null");
        }

        if (to == null) {
            throw new IllegalArgumentException("to road must not be null");
        }

        if (from == to) {
            throw new IllegalArgumentException("movement must start and end on different roads");
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

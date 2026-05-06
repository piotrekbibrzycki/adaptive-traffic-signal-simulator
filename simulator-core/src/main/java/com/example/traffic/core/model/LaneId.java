package com.example.traffic.core.model;

public record LaneId(Road road, LaneType laneType) {
    public LaneId {
        if (road == null) {
            throw new IllegalArgumentException("Road cannot be null");
        }

        if (laneType == null) {
            throw new IllegalArgumentException("Lane type cannot be null");
        }
    }
}

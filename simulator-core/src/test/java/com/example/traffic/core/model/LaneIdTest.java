package com.example.traffic.core.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LaneIdTest {
    @Test
    void rejectsNullRoad() {
        assertThatThrownBy(() -> new LaneId(null, LaneType.STRAIGHT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("road");
    }

    @Test
    void rejectsNullLaneType() {
        assertThatThrownBy(() -> new LaneId(Road.NORTH, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("lane type");
    }
}

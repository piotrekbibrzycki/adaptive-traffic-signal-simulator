package com.example.traffic.core.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TurnDirectionTest {
    @Test
    void resolvesStraightDirection() {
        assertThat(TurnDirection.fromRoads(Road.NORTH, Road.SOUTH))
                .isEqualTo(TurnDirection.STRAIGHT);
    }

    @Test
    void resolvesLeftDirection() {
        assertThat(TurnDirection.fromRoads(Road.NORTH, Road.EAST))
                .isEqualTo(TurnDirection.LEFT);
    }

    @Test
    void resolvesRightDirection() {
        assertThat(TurnDirection.fromRoads(Road.NORTH, Road.WEST))
                .isEqualTo(TurnDirection.RIGHT);
    }

    @Test
    void rejectsSameStartAndEndRoad() {
        assertThatThrownBy(() -> TurnDirection.fromRoads(Road.NORTH, Road.NORTH))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("different");
    }
}

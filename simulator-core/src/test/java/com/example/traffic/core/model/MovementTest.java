package com.example.traffic.core.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MovementTest {
    @Test
    void resolvesStraightMovement() {
        Movement movement = new Movement(Road.NORTH, Road.SOUTH);

        assertThat(movement.turnDirection()).isEqualTo(TurnDirection.STRAIGHT);
        assertThat(movement.requiredLane()).isEqualTo(LaneType.STRAIGHT);
    }

    @Test
    void resolvesLeftMovement() {
        Movement movement = new Movement(Road.NORTH, Road.EAST);

        assertThat(movement.turnDirection()).isEqualTo(TurnDirection.LEFT);
        assertThat(movement.requiredLane()).isEqualTo(LaneType.LEFT);
    }

    @Test
    void resolvesRightMovement() {
        Movement movement = new Movement(Road.NORTH, Road.WEST);

        assertThat(movement.turnDirection()).isEqualTo(TurnDirection.RIGHT);
        assertThat(movement.requiredLane()).isEqualTo(LaneType.RIGHT);
    }

    @Test
    void rejectsMovementToSameRoad() {
        assertThatThrownBy(() -> new Movement(Road.NORTH, Road.NORTH))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("different roads");
    }
}

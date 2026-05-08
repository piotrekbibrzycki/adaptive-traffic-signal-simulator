package com.example.traffic.core.safety;

import com.example.traffic.core.model.Movement;
import com.example.traffic.core.model.Road;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConflictMatrixTest {
    private final ConflictMatrix conflictMatrix = new ConflictMatrix();

    @Test
    void oppositeStraightMovementsDoNotConflict() {
        assertThat(conflictMatrix.conflicts(
                new Movement(Road.NORTH, Road.SOUTH),
                new Movement(Road.SOUTH, Road.NORTH)
        )).isFalse();
    }

    @Test
    void oppositeRightTurnsDoNotConflict() {
        assertThat(conflictMatrix.conflicts(
                new Movement(Road.NORTH, Road.WEST),
                new Movement(Road.SOUTH, Road.EAST)
        )).isFalse();
    }

    @Test
    void crossingStraightMovementsConflict() {
        assertThat(conflictMatrix.conflicts(
                new Movement(Road.NORTH, Road.SOUTH),
                new Movement(Road.EAST, Road.WEST)
        )).isTrue();
    }

    @Test
    void protectedLeftTurnsAreConservativeAndConflictWhenPaired() {
        assertThat(conflictMatrix.conflicts(
                new Movement(Road.NORTH, Road.EAST),
                new Movement(Road.SOUTH, Road.WEST)
        )).isTrue();
    }
}

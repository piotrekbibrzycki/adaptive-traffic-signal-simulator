package com.example.traffic.core.phase;

import com.example.traffic.core.model.Movement;
import com.example.traffic.core.model.Road;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PhaseTest {
    @Test
    void checksWhetherMovementIsAllowed() {
        Phase phase = new Phase(
                "TEST",
                Set.of(new Movement(Road.NORTH, Road.SOUTH)),
                1,
                3,
                0,
                0,
                1
        );

        assertThat(phase.allows(Road.NORTH, Road.SOUTH)).isTrue();
        assertThat(phase.allows(Road.EAST, Road.WEST)).isFalse();
    }

    @Test
    void rejectsEmptyAllowedMovements() {
        assertThatThrownBy(() -> new Phase("TEST", Set.of(), 1, 3, 0, 0, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("allowed movements");
    }

    @Test
    void rejectsInvalidGreenRange() {
        assertThatThrownBy(() -> new Phase(
                "TEST",
                Set.of(new Movement(Road.NORTH, Road.SOUTH)),
                4,
                3,
                0,
                0,
                1
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("max green");
    }

    @Test
    void copiesAllowedMovementsSet() {
        Set<Movement> movements = new java.util.LinkedHashSet<>();
        movements.add(new Movement(Road.NORTH, Road.SOUTH));

        Phase phase = new Phase("TEST", movements, 1, 3, 0, 0, 1);

        movements.add(new Movement(Road.SOUTH, Road.NORTH));

        assertThat(phase.allowedMovements())
                .containsExactly(new Movement(Road.NORTH, Road.SOUTH));
    }
}

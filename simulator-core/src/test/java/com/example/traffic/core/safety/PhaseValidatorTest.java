package com.example.traffic.core.safety;

import com.example.traffic.core.exception.SimulationException;
import com.example.traffic.core.model.Movement;
import com.example.traffic.core.model.Road;
import com.example.traffic.core.phase.DefaultPhases;
import com.example.traffic.core.phase.Phase;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PhaseValidatorTest {
    private final PhaseValidator validator = new PhaseValidator();

    @Test
    void acceptsDefaultPhases() {
        assertThatCode(() -> validator.validateAll(DefaultPhases.createDefault()))
                .doesNotThrowAnyException();
    }

    @Test
    void rejectsPhaseWithCrossingMovements() {
        Phase invalidPhase = new Phase(
                "INVALID_CROSSING",
                new LinkedHashSet<>(List.of(
                        new Movement(Road.NORTH, Road.SOUTH),
                        new Movement(Road.EAST, Road.WEST)
                )),
                1,
                3,
                0,
                0,
                1
        );

        assertThatThrownBy(() -> validator.validate(invalidPhase))
                .isInstanceOf(SimulationException.class)
                .hasMessageContaining("conflicting movements");
    }

    @Test
    void rejectsPhaseWithLeftTurnAndOpposingStraightMovement() {
        Phase invalidPhase = new Phase(
                "INVALID_LEFT_WITH_OPPOSING_STRAIGHT",
                new LinkedHashSet<>(List.of(
                        new Movement(Road.NORTH, Road.EAST),
                        new Movement(Road.SOUTH, Road.NORTH)
                )),
                1,
                3,
                0,
                0,
                1
        );

        assertThatThrownBy(() -> validator.validate(invalidPhase))
                .isInstanceOf(SimulationException.class)
                .hasMessageContaining("conflicting movements");
    }
}

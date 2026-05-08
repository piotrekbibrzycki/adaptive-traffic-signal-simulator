package com.example.traffic.core.phase;

import com.example.traffic.core.model.Road;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultPhasesTest {
    @Test
    void createsDefaultPhasesInDeterministicOrder() {
        List<Phase> phases = DefaultPhases.createDefault();

        assertThat(phases)
                .extracting(Phase::id)
                .containsExactly(
                        DefaultPhases.NS_STRAIGHT_RIGHT,
                        DefaultPhases.EW_STRAIGHT_RIGHT,
                        DefaultPhases.N_LEFT,
                        DefaultPhases.S_LEFT,
                        DefaultPhases.E_LEFT,
                        DefaultPhases.W_LEFT
                );
    }

    @Test
    void northSouthPhaseAllowsStraightAndRightMovements() {
        Phase phase = DefaultPhases.createDefault().getFirst();

        assertThat(phase.allows(Road.NORTH, Road.SOUTH)).isTrue();
        assertThat(phase.allows(Road.SOUTH, Road.NORTH)).isTrue();
        assertThat(phase.allows(Road.NORTH, Road.WEST)).isTrue();
        assertThat(phase.allows(Road.SOUTH, Road.EAST)).isTrue();
    }

    @Test
    void eastWestPhaseAllowsStraightAndRightMovements() {
        Phase phase = DefaultPhases.createDefault().get(1);

        assertThat(phase.allows(Road.EAST, Road.WEST)).isTrue();
        assertThat(phase.allows(Road.WEST, Road.EAST)).isTrue();
        assertThat(phase.allows(Road.EAST, Road.NORTH)).isTrue();
        assertThat(phase.allows(Road.WEST, Road.SOUTH)).isTrue();
    }

    @Test
    void leftTurnPhasesAllowSingleProtectedLeftTurn() {
        List<Phase> phases = DefaultPhases.createDefault();

        assertThat(phases.get(2).allows(Road.NORTH, Road.EAST)).isTrue();
        assertThat(phases.get(3).allows(Road.SOUTH, Road.WEST)).isTrue();
        assertThat(phases.get(4).allows(Road.EAST, Road.SOUTH)).isTrue();
        assertThat(phases.get(5).allows(Road.WEST, Road.NORTH)).isTrue();
    }
}

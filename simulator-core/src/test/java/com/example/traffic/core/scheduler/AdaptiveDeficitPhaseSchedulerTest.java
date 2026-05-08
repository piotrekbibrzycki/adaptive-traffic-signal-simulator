package com.example.traffic.core.scheduler;

import com.example.traffic.core.intersection.IntersectionState;
import com.example.traffic.core.model.Movement;
import com.example.traffic.core.model.Road;
import com.example.traffic.core.model.Vehicle;
import com.example.traffic.core.model.VehiclePriority;
import com.example.traffic.core.phase.DefaultPhases;
import com.example.traffic.core.phase.Phase;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AdaptiveDeficitPhaseSchedulerTest {
    @Test
    void keepsCurrentPhaseBeforeMinimumGreenTime() {
        Phase nsPhase = new Phase(
                "TEST_NS",
                new LinkedHashSet<>(List.of(
                        new Movement(Road.NORTH, Road.SOUTH),
                        new Movement(Road.SOUTH, Road.NORTH)
                )),
                2,
                8,
                0,
                0,
                1
        );

        Phase ewPhase = new Phase(
                "TEST_EW",
                new LinkedHashSet<>(List.of(
                        new Movement(Road.EAST, Road.WEST),
                        new Movement(Road.WEST, Road.EAST)
                )),
                2,
                8,
                0,
                0,
                1
        );

        List<Phase> phases = List.of(nsPhase, ewPhase);

        IntersectionState state = new IntersectionState();
        state.enqueue(new Vehicle("vehicle1", Road.WEST, Road.EAST, 0, VehiclePriority.NORMAL));

        AdaptiveDeficitPhaseScheduler scheduler = new AdaptiveDeficitPhaseScheduler();

        Phase selectedPhase = scheduler.selectPhase(phases, state, nsPhase, 1);

        assertThat(selectedPhase).isEqualTo(nsPhase);
    }

    @Test
    void switchesAfterMaximumGreenTime() {
        List<Phase> phases = DefaultPhases.createDefault();
        Phase currentPhase = phaseById(phases, DefaultPhases.NS_STRAIGHT_RIGHT);
        Phase expectedPhase = phaseById(phases, DefaultPhases.EW_STRAIGHT_RIGHT);

        IntersectionState state = new IntersectionState();
        state.enqueue(new Vehicle("vehicle1", Road.WEST, Road.EAST, 0, VehiclePriority.NORMAL));

        AdaptiveDeficitPhaseScheduler scheduler = new AdaptiveDeficitPhaseScheduler();

        Phase selectedPhase = scheduler.selectPhase(phases, state, currentPhase, currentPhase.maxGreenSteps());

        assertThat(selectedPhase).isEqualTo(expectedPhase);
    }

    @Test
    void prefersPhaseWithLargerServiceableQueue() {
        List<Phase> phases = DefaultPhases.createDefault();
        Phase currentPhase = phaseById(phases, DefaultPhases.NS_STRAIGHT_RIGHT);
        Phase expectedPhase = phaseById(phases, DefaultPhases.EW_STRAIGHT_RIGHT);

        IntersectionState state = new IntersectionState();
        state.enqueue(new Vehicle("vehicle1", Road.WEST, Road.EAST, 0, VehiclePriority.NORMAL));
        state.enqueue(new Vehicle("vehicle2", Road.EAST, Road.WEST, 0, VehiclePriority.NORMAL));

        AdaptiveSchedulerConfig config = new AdaptiveSchedulerConfig(
                10,
                0,
                0,
                0,
                1,
                0
        );

        AdaptiveDeficitPhaseScheduler scheduler = new AdaptiveDeficitPhaseScheduler(config);

        Phase selectedPhase = scheduler.selectPhase(phases, state, currentPhase, currentPhase.minGreenSteps());

        assertThat(selectedPhase).isEqualTo(expectedPhase);
    }

    @Test
    void eventuallyServesSkippedWaitingPhase() {
        List<Phase> phases = DefaultPhases.createDefault();
        Phase nsPhase = phaseById(phases, DefaultPhases.NS_STRAIGHT_RIGHT);
        Phase ewPhase = phaseById(phases, DefaultPhases.EW_STRAIGHT_RIGHT);

        IntersectionState state = new IntersectionState();
        state.enqueue(new Vehicle("vehicle1", Road.WEST, Road.EAST, 0, VehiclePriority.NORMAL));

        AdaptiveSchedulerConfig config = new AdaptiveSchedulerConfig(
                0,
                0,
                10,
                0,
                1,
                0
        );

        AdaptiveDeficitPhaseScheduler scheduler = new AdaptiveDeficitPhaseScheduler(config);

        Phase selected = nsPhase;
        for (int i = 0; i < 3; i++) {
            selected = scheduler.selectPhase(phases, state, nsPhase, nsPhase.minGreenSteps());
        }

        assertThat(selected).isEqualTo(ewPhase);
    }

    private Phase phaseById(List<Phase> phases, String id) {
        return phases.stream()
                .filter(phase -> phase.id().equals(id))
                .findFirst()
                .orElseThrow();
    }
}

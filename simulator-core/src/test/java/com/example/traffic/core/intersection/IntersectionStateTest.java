package com.example.traffic.core.intersection;

import com.example.traffic.core.model.Movement;
import com.example.traffic.core.model.Road;
import com.example.traffic.core.model.Vehicle;
import com.example.traffic.core.model.VehiclePriority;
import com.example.traffic.core.phase.Phase;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class IntersectionStateTest {
    @Test
    void countsServiceableVehiclesForPhase() {
        IntersectionState state = new IntersectionState();
        Phase phase = new Phase(
                "TEST_NS",
                new LinkedHashSet<>(List.of(
                        new Movement(Road.NORTH, Road.SOUTH),
                        new Movement(Road.SOUTH, Road.NORTH)
                )),
                1,
                3,
                0,
                0,
                1
        );

        state.enqueue(new Vehicle("vehicle1", Road.NORTH, Road.SOUTH, 0, VehiclePriority.NORMAL));
        state.enqueue(new Vehicle("vehicle2", Road.SOUTH, Road.NORTH, 0, VehiclePriority.NORMAL));
        state.enqueue(new Vehicle("vehicle3", Road.WEST, Road.EAST, 0, VehiclePriority.NORMAL));

        assertThat(state.serviceableVehicles(phase)).isEqualTo(2);
    }

    @Test
    void calculatesMaxWaitingTimeForServiceableVehicles() {
        IntersectionState state = new IntersectionState();
        Phase phase = new Phase(
                "TEST_NS",
                new LinkedHashSet<>(List.of(
                        new Movement(Road.NORTH, Road.SOUTH),
                        new Movement(Road.SOUTH, Road.NORTH)
                )),
                1,
                3,
                0,
                0,
                1
        );

        state.enqueue(new Vehicle("vehicle1", Road.NORTH, Road.SOUTH, 2, VehiclePriority.NORMAL));
        state.enqueue(new Vehicle("vehicle2", Road.SOUTH, Road.NORTH, 7, VehiclePriority.NORMAL));
        state.enqueue(new Vehicle("vehicle3", Road.WEST, Road.EAST, 0, VehiclePriority.NORMAL));

        assertThat(state.maxWaitingTime(phase, 10)).isEqualTo(8);
    }

    @Test
    void returnsZeroWaitingTimeWhenPhaseHasNoServiceableVehicles() {
        IntersectionState state = new IntersectionState();
        Phase phase = new Phase(
                "TEST_NS",
                new LinkedHashSet<>(List.of(
                        new Movement(Road.NORTH, Road.SOUTH),
                        new Movement(Road.SOUTH, Road.NORTH)
                )),
                1,
                3,
                0,
                0,
                1
        );

        state.enqueue(new Vehicle("vehicle1", Road.WEST, Road.EAST, 2, VehiclePriority.NORMAL));

        assertThat(state.serviceableVehicles(phase)).isZero();
        assertThat(state.maxWaitingTime(phase, 10)).isZero();
    }
}

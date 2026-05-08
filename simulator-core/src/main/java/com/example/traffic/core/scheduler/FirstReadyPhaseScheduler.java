package com.example.traffic.core.scheduler;

import com.example.traffic.core.intersection.IntersectionState;
import com.example.traffic.core.phase.Phase;

import java.util.List;

public final class FirstReadyPhaseScheduler implements PhaseScheduler {
    @Override
    public Phase selectPhase(
            List<Phase> phases,
            IntersectionState intersectionState,
            Phase currentPhase,
            int currentPhaseElapsed
    ) {
        if (phases == null || phases.isEmpty()) {
            throw new IllegalArgumentException("phases must not be null or empty");
        }

        if (intersectionState == null) {
            throw new IllegalArgumentException("intersection state must not be null");
        }

        for (Phase phase : phases) {
            if (intersectionState.hasWaitingVehicleFor(phase)) {
                return phase;
            }
        }

        return currentPhase == null ? phases.getFirst() : currentPhase;
    }
}

package com.example.traffic.core.scheduler;

import com.example.traffic.core.intersection.IntersectionState;
import com.example.traffic.core.phase.Phase;

import java.util.List;

public interface PhaseScheduler {
    Phase selectPhase(List<Phase> phases, IntersectionState intersectionState,
                      Phase currentPhase, int currentPhaseElapsed);
}

package com.example.traffic.core.scheduler;

import com.example.traffic.core.intersection.IntersectionState;
import com.example.traffic.core.phase.Phase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AdaptiveDeficitPhaseScheduler implements PhaseScheduler {
    private final AdaptiveSchedulerConfig config;
    private final Map<String, Integer> deficits = new HashMap<>();
    private final Map<String, Integer> skippedRounds = new HashMap<>();
    private int decisionStep = 0;

    public AdaptiveDeficitPhaseScheduler() {
        this(AdaptiveSchedulerConfig.defaultConfig());
    }

    public AdaptiveDeficitPhaseScheduler(AdaptiveSchedulerConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("config must not be null");
        }

        this.config = config;
    }

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

        Phase effectiveCurrentPhase = currentPhase == null ? phases.getFirst() : currentPhase;

        updateDeficits(phases, intersectionState);
        Phase bestPhase = findBestPhase(phases, intersectionState, effectiveCurrentPhase);

        if (bestPhase == null) {
            decisionStep++;
            return effectiveCurrentPhase;
        }

        Phase selectedPhase = selectWithGreenConstraints(effectiveCurrentPhase, currentPhaseElapsed, bestPhase);
        updateSkippedRounds(phases, selectedPhase, intersectionState);

        int servedVehicles = intersectionState.serviceableVehicles(selectedPhase);
        subtractServiceCost(selectedPhase, servedVehicles);

        decisionStep++;
        return selectedPhase;
    }

    private void updateDeficits(List<Phase> phases, IntersectionState intersectionState) {
        for (Phase phase : phases) {
            int serviceableVehicles = intersectionState.serviceableVehicles(phase);

            if (serviceableVehicles > 0) {
                int dynamicWeight = phase.baseWeight()
                        + config.queueWeight() * serviceableVehicles
                        + config.waitingTimeWeight() * intersectionState.maxWaitingTime(phase, decisionStep)
                        + config.skippedPhaseWeight() * skippedRounds.getOrDefault(phase.id(), 0);

                deficits.merge(phase.id(), dynamicWeight, Integer::sum);
            } else {
                int currentDeficit = deficits.getOrDefault(phase.id(), 0);
                deficits.put(phase.id(), Math.max(0, currentDeficit - config.idleDeficitDecay()));
            }
        }
    }

    private Phase findBestPhase(
            List<Phase> phases,
            IntersectionState intersectionState,
            Phase currentPhase
    ) {
        Phase bestPhase = null;

        for (Phase phase : phases) {
            if (!intersectionState.hasWaitingVehicleFor(phase)) {
                continue;
            }

            if (bestPhase == null || isBetterCandidate(phase, bestPhase, currentPhase, intersectionState)) {
                bestPhase = phase;
            }
        }

        return bestPhase;
    }

    private boolean isBetterCandidate(
            Phase candidate,
            Phase currentBest,
            Phase currentPhase,
            IntersectionState intersectionState
    ) {
        if (candidate.equals(currentPhase) && !currentBest.equals(currentPhase)) {
            return true;
        }

        if (!candidate.equals(currentPhase) && currentBest.equals(currentPhase)) {
            return false;
        }

        int candidateDeficit = deficits.getOrDefault(candidate.id(), 0);
        int bestDeficit = deficits.getOrDefault(currentBest.id(), 0);
        if (candidateDeficit != bestDeficit) {
            return candidateDeficit > bestDeficit;
        }

        int candidateWaiting = intersectionState.maxWaitingTime(candidate, decisionStep);
        int bestWaiting = intersectionState.maxWaitingTime(currentBest, decisionStep);
        if (candidateWaiting != bestWaiting) {
            return candidateWaiting > bestWaiting;
        }

        int candidateQueue = intersectionState.serviceableVehicles(candidate);
        int bestQueue = intersectionState.serviceableVehicles(currentBest);
        return candidateQueue > bestQueue;
    }

    private Phase selectWithGreenConstraints(
            Phase currentPhase,
            int currentPhaseElapsed,
            Phase bestPhase
    ) {
        if (currentPhaseElapsed < currentPhase.minGreenSteps()) {
            return currentPhase;
        }

        if (currentPhaseElapsed >= currentPhase.maxGreenSteps()) {
            return bestPhase;
        }

        if (bestPhase.equals(currentPhase)) {
            return currentPhase;
        }

        int bestDeficit = deficits.getOrDefault(bestPhase.id(), 0);
        int currentDeficit = deficits.getOrDefault(currentPhase.id(), 0);

        if (bestDeficit > currentDeficit + config.switchThreshold()) {
            return bestPhase;
        }

        return currentPhase;
    }

    private void updateSkippedRounds(
            List<Phase> phases,
            Phase selectedPhase,
            IntersectionState intersectionState
    ) {
        for (Phase phase : phases) {
            if (phase.equals(selectedPhase)) {
                skippedRounds.put(phase.id(), 0);
            } else if (intersectionState.hasWaitingVehicleFor(phase)) {
                skippedRounds.merge(phase.id(), 1, Integer::sum);
            }
        }
    }

    private void subtractServiceCost(Phase selectedPhase, int servedVehicles) {
        int currentDeficit = deficits.getOrDefault(selectedPhase.id(), 0);
        int cost = servedVehicles * config.serviceCostPerVehicle();

        deficits.put(selectedPhase.id(), Math.max(0, currentDeficit - cost));
    }
}

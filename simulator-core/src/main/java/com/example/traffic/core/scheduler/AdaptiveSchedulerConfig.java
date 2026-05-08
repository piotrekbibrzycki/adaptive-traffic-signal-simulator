package com.example.traffic.core.scheduler;

public record AdaptiveSchedulerConfig(
        int queueWeight,
        int waitingTimeWeight,
        int skippedPhaseWeight,
        int switchThreshold,
        int serviceCostPerVehicle,
        int idleDeficitDecay
) {
    public AdaptiveSchedulerConfig {
        if (queueWeight < 0) {
            throw new IllegalArgumentException("queue weight must not be negative");
        }

        if (waitingTimeWeight < 0) {
            throw new IllegalArgumentException("waiting time weight must not be negative");
        }

        if (skippedPhaseWeight < 0) {
            throw new IllegalArgumentException("skipped phase weight must not be negative");
        }

        if (switchThreshold < 0) {
            throw new IllegalArgumentException("switch threshold must not be negative");
        }

        if (serviceCostPerVehicle < 1) {
            throw new IllegalArgumentException("service cost per vehicle must be at least 1");
        }

        if (idleDeficitDecay < 0) {
            throw new IllegalArgumentException("idle deficit decay must not be negative");
        }
    }

    public static AdaptiveSchedulerConfig defaultConfig() {
        return new AdaptiveSchedulerConfig(
                3,
                2,
                1,
                3,
                4,
                1
        );
    }
}

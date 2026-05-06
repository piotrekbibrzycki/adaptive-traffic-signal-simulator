package com.example.traffic.core.phase;

import com.example.traffic.core.model.Movement;
import com.example.traffic.core.model.Road;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public record Phase(
        String id,
        Set<Movement> allowedMovements,
        int minGreenSteps,
        int maxGreenSteps,
        int yellowSteps,
        int allRedSteps,
        int baseWeight
) {
    public Phase {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("phase id must not be null or blank");
        }

        if (allowedMovements == null || allowedMovements.isEmpty()) {
            throw new IllegalArgumentException("allowed movements must not be null or empty");
        }

        if (minGreenSteps < 1) {
            throw new IllegalArgumentException("min green steps must be at least 1");
        }

        if (maxGreenSteps < minGreenSteps) {
            throw new IllegalArgumentException("max green steps must be greater than or equal to min green steps");
        }

        if (yellowSteps < 0) {
            throw new IllegalArgumentException("yellow steps must not be negative");
        }

        if (allRedSteps < 0) {
            throw new IllegalArgumentException("all-red steps must not be negative");
        }

        if (baseWeight < 1) {
            throw new IllegalArgumentException("base weight must be at least 1");
        }

        allowedMovements = Collections.unmodifiableSet(new LinkedHashSet<>(allowedMovements));
    }

    public boolean allows(Road from, Road to) {
        return allowedMovements.contains(new Movement(from, to));
    }
}

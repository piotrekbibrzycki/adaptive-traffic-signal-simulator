package com.example.traffic.core.safety;

import com.example.traffic.core.exception.SimulationException;
import com.example.traffic.core.model.Movement;
import com.example.traffic.core.phase.Phase;

import java.util.ArrayList;
import java.util.List;

public final class PhaseValidator {
    private final ConflictMatrix conflictMatrix;

    public PhaseValidator() {
        this(new ConflictMatrix());
    }

    public PhaseValidator(ConflictMatrix conflictMatrix) {
        if (conflictMatrix == null) {
            throw new IllegalArgumentException("conflict matrix must not be null");
        }

        this.conflictMatrix = conflictMatrix;
    }

    public void validate(Phase phase) {
        if (phase == null) {
            throw new IllegalArgumentException("phase must not be null");
        }

        List<Movement> movements = new ArrayList<>(phase.allowedMovements());

        for (int firstIndex = 0; firstIndex < movements.size(); firstIndex++) {
            for (int secondIndex = firstIndex + 1; secondIndex < movements.size(); secondIndex++) {
                Movement first = movements.get(firstIndex);
                Movement second = movements.get(secondIndex);

                if (conflictMatrix.conflicts(first, second)) {
                    throw new SimulationException(
                            "phase " + phase.id() + " contains conflicting movements: " + first + " and " + second
                    );
                }
            }
        }
    }

    public void validateAll(List<Phase> phases) {
        if (phases == null) {
            throw new IllegalArgumentException("phases must not be null");
        }

        for (Phase phase : phases) {
            validate(phase);
        }
    }
}

package com.example.traffic.core.safety;

import com.example.traffic.core.model.LaneId;
import com.example.traffic.core.model.Movement;
import com.example.traffic.core.model.TurnDirection;

public final class ConflictMatrix {
    public boolean conflicts(Movement first, Movement second) {
        if (first == null) {
            throw new IllegalArgumentException("first movement must not be null");
        }

        if (second == null) {
            throw new IllegalArgumentException("second movement must not be null");
        }

        if (first.equals(second)) {
            return false;
        }

        if (sameRequiredLane(first, second)) {
            return true;
        }

        if (sameApproachStraightOrRight(first, second)) {
            return false;
        }

        if (oppositeApproachStraightOrRight(first, second)) {
            return false;
        }

        return true;
    }

    private boolean sameRequiredLane(Movement first, Movement second) {
        LaneId firstLane = new LaneId(first.from(), first.requiredLane());
        LaneId secondLane = new LaneId(second.from(), second.requiredLane());

        return firstLane.equals(secondLane);
    }

    private boolean sameApproachStraightOrRight(Movement first, Movement second) {
        if (first.from() != second.from()) {
            return false;
        }

        return isStraightOrRight(first) && isStraightOrRight(second);
    }

    private boolean oppositeApproachStraightOrRight(Movement first, Movement second) {
        if (first.from().opposite() != second.from()) {
            return false;
        }

        return isStraightOrRight(first) && isStraightOrRight(second);
    }

    private boolean isStraightOrRight(Movement movement) {
        TurnDirection direction = movement.turnDirection();
        return direction == TurnDirection.STRAIGHT || direction == TurnDirection.RIGHT;
    }
}

package com.example.traffic.core.intersection;

import com.example.traffic.core.model.LaneId;
import com.example.traffic.core.model.LaneType;
import com.example.traffic.core.model.Movement;
import com.example.traffic.core.model.Road;
import com.example.traffic.core.model.Vehicle;
import com.example.traffic.core.phase.Phase;

import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;

public final class IntersectionState {
    private final Map<LaneId, Queue<Vehicle>> queues = new LinkedHashMap<>();

    public IntersectionState() {
        for (Road road : Road.values()) {
            for (LaneType laneType : LaneType.values()) {
                queues.put(new LaneId(road, laneType), new ArrayDeque<>());
            }
        }
    }

    public void enqueue(Vehicle vehicle) {
        Movement movement = new Movement(vehicle.startRoad(), vehicle.endRoad());
        LaneId laneId = new LaneId(vehicle.startRoad(), movement.requiredLane());

        queues.get(laneId).add(vehicle);
    }

    public Vehicle poll(Movement movement) {
        LaneId laneId = new LaneId(movement.from(), movement.requiredLane());
        Queue<Vehicle> queue = queues.get(laneId);

        Vehicle first = queue.peek();
        if (first == null) {
            return null;
        }

        Movement firstMovement = new Movement(first.startRoad(), first.endRoad());
        if (!firstMovement.equals(movement)) {
            return null;
        }

        return queue.poll();
    }

    public boolean hasWaitingVehicle(Movement movement) {
        return peek(movement) != null;
    }

    public boolean hasWaitingVehicleFor(Phase phase) {
        return serviceableVehicles(phase) > 0;
    }

    public int serviceableVehicles(Phase phase) {
        if (phase == null) {
            throw new IllegalArgumentException("phase must not be null");
        }

        int count = 0;

        for (Movement movement : phase.allowedMovements()) {
            if (hasWaitingVehicle(movement)) {
                count++;
            }
        }

        return count;
    }

    public int maxWaitingTime(Phase phase, int currentStep) {
        if (phase == null) {
            throw new IllegalArgumentException("phase must not be null");
        }

        int maxWaitingTime = 0;

        for (Movement movement : phase.allowedMovements()) {
            Vehicle vehicle = peek(movement);
            if (vehicle != null) {
                int waitingTime = currentStep - vehicle.createdAtStep();
                maxWaitingTime = Math.max(maxWaitingTime, waitingTime);
            }
        }

        return maxWaitingTime;
    }

    private Vehicle peek(Movement movement) {
        LaneId laneId = new LaneId(movement.from(), movement.requiredLane());
        Queue<Vehicle> queue = queues.get(laneId);

        Vehicle first = queue.peek();
        if (first == null) {
            return null;
        }

        Movement firstMovement = new Movement(first.startRoad(), first.endRoad());
        if (!firstMovement.equals(movement)) {
            return null;
        }

        return first;
    }
}

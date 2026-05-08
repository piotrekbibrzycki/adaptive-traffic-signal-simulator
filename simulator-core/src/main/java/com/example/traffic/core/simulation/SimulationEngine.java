package com.example.traffic.core.simulation;

import com.example.traffic.core.command.AddVehicleCommand;
import com.example.traffic.core.command.Command;
import com.example.traffic.core.command.StepCommand;
import com.example.traffic.core.exception.SimulationException;
import com.example.traffic.core.intersection.IntersectionState;
import com.example.traffic.core.model.Movement;
import com.example.traffic.core.model.Vehicle;
import com.example.traffic.core.phase.DefaultPhases;
import com.example.traffic.core.phase.Phase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class SimulationEngine {
    private final List<Phase> phases;

    public SimulationEngine() {
        this(DefaultPhases.createDefault());
    }

    public SimulationEngine(List<Phase> phases) {
        if (phases == null || phases.isEmpty()) {
            throw new IllegalArgumentException("phases must not be null or empty");
        }

        this.phases = List.copyOf(phases);
    }

    public SimulationOutput run(List<Command> commands) {
        if (commands == null) {
            throw new IllegalArgumentException("commands must not be null");
        }

        IntersectionState intersectionState = new IntersectionState();
        Set<String> knownVehicleIds = new HashSet<>();
        Map<String, Integer> vehicleArrivalOrder = new HashMap<>();
        List<StepStatus> stepStatuses = new ArrayList<>();

        int currentStep = 0;

        for (Command command : commands) {
            if (command instanceof AddVehicleCommand addVehicleCommand) {
                handleAddVehicle(
                        addVehicleCommand,
                        intersectionState,
                        knownVehicleIds,
                        vehicleArrivalOrder,
                        currentStep
                );
            } else if (command instanceof StepCommand) {
                StepStatus stepStatus = handleStep(intersectionState, vehicleArrivalOrder);
                stepStatuses.add(stepStatus);
                currentStep++;
            } else {
                throw new SimulationException("unsupported command type: " + command.getClass().getName());
            }
        }

        return new SimulationOutput(stepStatuses);
    }

    private void handleAddVehicle(
            AddVehicleCommand command,
            IntersectionState intersectionState,
            Set<String> knownVehicleIds,
            Map<String, Integer> vehicleArrivalOrder,
            int currentStep
    ) {
        if (!knownVehicleIds.add(command.vehicleId())) {
            throw new SimulationException("duplicate vehicle id: " + command.vehicleId());
        }

        vehicleArrivalOrder.put(command.vehicleId(), vehicleArrivalOrder.size());

        Vehicle vehicle = new Vehicle(
                command.vehicleId(),
                command.startRoad(),
                command.endRoad(),
                currentStep,
                command.priority()
        );

        intersectionState.enqueue(vehicle);
    }

    private StepStatus handleStep(
            IntersectionState intersectionState,
            Map<String, Integer> vehicleArrivalOrder
    ) {
        Phase selectedPhase = selectPhase(intersectionState);
        List<Vehicle> leftVehicles = new ArrayList<>();

        for (Movement movement : selectedPhase.allowedMovements()) {
            Vehicle vehicle = intersectionState.poll(movement);
            if (vehicle != null) {
                leftVehicles.add(vehicle);
            }
        }

        List<String> leftVehicleIds = leftVehicles.stream()
                .sorted(Comparator.comparingInt(vehicle -> vehicleArrivalOrder.get(vehicle.id())))
                .map(Vehicle::id)
                .toList();

        return new StepStatus(leftVehicleIds);
    }

    private Phase selectPhase(IntersectionState intersectionState) {
        for (Phase phase : phases) {
            if (intersectionState.hasWaitingVehicleFor(phase)) {
                return phase;
            }
        }

        return phases.getFirst();
    }
}

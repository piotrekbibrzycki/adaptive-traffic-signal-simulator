package com.example.traffic.core.command;

public sealed interface Command permits AddVehicleCommand, StepCommand {
}

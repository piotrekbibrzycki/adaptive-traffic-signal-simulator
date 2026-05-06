package com.example.traffic.core.exception;

public class SimulationException extends RuntimeException {
    public SimulationException(String message) {
        super(message);
    }

    public SimulationException(String message, Throwable cause) {
        super(message, cause);
    }
}

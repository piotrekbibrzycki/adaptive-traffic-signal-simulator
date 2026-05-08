package com.example.traffic.cli.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CommandDto(
        String type,
        String vehicleId,
        String startRoad,
        String endRoad
) {
}

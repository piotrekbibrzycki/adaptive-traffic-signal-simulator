package com.example.traffic.core.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RoadTest {
    @Test
    void mapsRoadsFromJsonNames() {
        assertThat(Road.fromJsonName("north")).isEqualTo(Road.NORTH);
        assertThat(Road.fromJsonName("south")).isEqualTo(Road.SOUTH);
        assertThat(Road.fromJsonName("east")).isEqualTo(Road.EAST);
        assertThat(Road.fromJsonName("west")).isEqualTo(Road.WEST);
    }

    @Test
    void mapsRoadsToJsonNames() {
        assertThat(Road.NORTH.toJsonName()).isEqualTo("north");
        assertThat(Road.SOUTH.toJsonName()).isEqualTo("south");
        assertThat(Road.EAST.toJsonName()).isEqualTo("east");
        assertThat(Road.WEST.toJsonName()).isEqualTo("west");
    }

    @Test
    void resolvesOppositeLeftAndRightFromVehiclePerspective() {
        assertThat(Road.NORTH.opposite()).isEqualTo(Road.SOUTH);
        assertThat(Road.NORTH.left()).isEqualTo(Road.EAST);
        assertThat(Road.NORTH.right()).isEqualTo(Road.WEST);

        assertThat(Road.SOUTH.opposite()).isEqualTo(Road.NORTH);
        assertThat(Road.SOUTH.left()).isEqualTo(Road.WEST);
        assertThat(Road.SOUTH.right()).isEqualTo(Road.EAST);

        assertThat(Road.EAST.opposite()).isEqualTo(Road.WEST);
        assertThat(Road.EAST.left()).isEqualTo(Road.SOUTH);
        assertThat(Road.EAST.right()).isEqualTo(Road.NORTH);

        assertThat(Road.WEST.opposite()).isEqualTo(Road.EAST);
        assertThat(Road.WEST.left()).isEqualTo(Road.NORTH);
        assertThat(Road.WEST.right()).isEqualTo(Road.SOUTH);
    }

    @Test
    void rejectsUnknownRoadName() {
        assertThatThrownBy(() -> Road.fromJsonName("test"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("unknown road");
    }
}

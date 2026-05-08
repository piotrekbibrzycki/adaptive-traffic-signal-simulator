package com.example.traffic.core.scheduler;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AdaptiveSchedulerConfigTest {
    @Test
    void createsDefaultConfig() {
        AdaptiveSchedulerConfig config = AdaptiveSchedulerConfig.defaultConfig();

        assertThat(config.queueWeight()).isPositive();
        assertThat(config.waitingTimeWeight()).isPositive();
        assertThat(config.skippedPhaseWeight()).isPositive();
        assertThat(config.switchThreshold()).isNotNegative();
        assertThat(config.serviceCostPerVehicle()).isPositive();
        assertThat(config.idleDeficitDecay()).isNotNegative();
    }

    @Test
    void rejectsNegativeQueueWeight() {
        assertThatThrownBy(() -> new AdaptiveSchedulerConfig(-1, 1, 1, 1, 1, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("queue weight");
    }

    @Test
    void rejectsInvalidServiceCost() {
        assertThatThrownBy(() -> new AdaptiveSchedulerConfig(1, 1, 1, 1, 0, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("service cost");
    }
}

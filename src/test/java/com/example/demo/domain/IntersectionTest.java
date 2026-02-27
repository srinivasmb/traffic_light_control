package com.example.demo.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionTest {

    private Intersection intersection;

    @BeforeEach
    void setUp() {
        intersection = new Intersection();
    }

    @Test
    void shouldInitializeWithAllRedLights() {
        Map<Direction, TrafficLight> lights = intersection.getLights();
        assertEquals(4, lights.size());
        for (Direction dir : Direction.values()) {
            assertEquals(LightState.RED, lights.get(dir).getState());
        }
    }

    @Test
    void shouldAllowNorthSouthGreenWhenEastWestRed() {
        assertDoesNotThrow(() -> {
            intersection.changeLightState(Direction.NORTH, LightState.GREEN);
            intersection.changeLightState(Direction.SOUTH, LightState.GREEN);
        });
        
        assertEquals(LightState.GREEN, intersection.getLightState(Direction.NORTH));
        assertEquals(LightState.GREEN, intersection.getLightState(Direction.SOUTH));
    }

    @Test
    void shouldThrowExceptionWhenConflictingLightsAreGreen() {
        intersection.changeLightState(Direction.NORTH, LightState.GREEN);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            intersection.changeLightState(Direction.EAST, LightState.GREEN);
        });

        assertEquals("Conflicting directions cannot be green or yellow simultaneously.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenConflictingLightsAreYellow() {
        intersection.changeLightState(Direction.WEST, LightState.YELLOW);

        assertThrows(IllegalStateException.class, () -> {
            intersection.changeLightState(Direction.NORTH, LightState.GREEN);
        });
        assertThrows(IllegalStateException.class, () -> {
            intersection.changeLightState(Direction.SOUTH, LightState.YELLOW);
        });
    }

    @Test
    void shouldAllowStateChangeWhenConflictingLightsAreRed() {
        intersection.changeLightState(Direction.NORTH, LightState.GREEN);
        intersection.changeLightState(Direction.NORTH, LightState.RED); // Change back to red

        assertDoesNotThrow(() -> {
            intersection.changeLightState(Direction.EAST, LightState.GREEN);
        });
        assertEquals(LightState.GREEN, intersection.getLightState(Direction.EAST));
    }
}

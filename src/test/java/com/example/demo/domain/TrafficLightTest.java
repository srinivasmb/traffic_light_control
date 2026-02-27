package com.example.demo.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrafficLightTest {

    @Test
    void shouldInitializeWithRedLight() {
        TrafficLight light = new TrafficLight(Direction.NORTH);
        assertEquals(Direction.NORTH, light.getDirection());
        assertEquals(LightState.RED, light.getState());
    }

    @Test
    void shouldChangeState() {
        TrafficLight light = new TrafficLight(Direction.NORTH);
        light.changeState(LightState.GREEN);
        assertEquals(LightState.GREEN, light.getState());

        light.changeState(LightState.YELLOW);
        assertEquals(LightState.YELLOW, light.getState());
    }
}

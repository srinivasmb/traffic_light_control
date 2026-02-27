package com.example.demo.domain;

public class TrafficLight {
    private final Direction direction;
    private LightState state;

    public TrafficLight(Direction direction) {
        this.direction = direction;
        this.state = LightState.RED; // Default state is safe
    }

    public Direction getDirection() {
        return direction;
    }

    public LightState getState() {
        return state;
    }

    public void changeState(LightState newState) {
        this.state = newState;
    }
}

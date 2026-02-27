package com.example.demo.dto;

import com.example.demo.domain.Direction;
import com.example.demo.domain.LightState;

public class CommandRequest {
    private String type; // "PAUSE", "RESUME", "CHANGE"
    private Direction direction;
    private LightState state;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public LightState getState() {
        return state;
    }

    public void setState(LightState state) {
        this.state = state;
    }
}

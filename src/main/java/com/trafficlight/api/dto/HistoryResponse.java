package com.trafficlight.api.dto;

import com.trafficlight.api.domain.Direction;
import com.trafficlight.api.domain.LightState;

import java.time.LocalDateTime;

public class HistoryResponse {
    private Direction direction;
    private LightState state;
    private LocalDateTime timestamp;

    public HistoryResponse(Direction direction, LightState state, LocalDateTime timestamp) {
        this.direction = direction;
        this.state = state;
        this.timestamp = timestamp;
    }

    public Direction getDirection() { return direction; }
    public LightState getState() { return state; }
    public LocalDateTime getTimestamp() { return timestamp; }
}

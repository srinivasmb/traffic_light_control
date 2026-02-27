package com.example.demo.entity;

import com.example.demo.domain.Direction;
import com.example.demo.domain.LightState;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class StateChangeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Direction direction;

    @Enumerated(EnumType.STRING)
    private LightState state;

    private LocalDateTime timestamp;

    public StateChangeHistory() {
    }

    public StateChangeHistory(Direction direction, LightState state, LocalDateTime timestamp) {
        this.direction = direction;
        this.state = state;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public Direction getDirection() {
        return direction;
    }

    public LightState getState() {
        return state;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

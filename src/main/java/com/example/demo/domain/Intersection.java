package com.example.demo.domain;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class Intersection {
    private final Map<Direction, TrafficLight> lights;

    public Intersection() {
        Map<Direction, TrafficLight> initialLights = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.values()) {
            initialLights.put(dir, new TrafficLight(dir));
        }
        this.lights = Collections.unmodifiableMap(initialLights);
    }

    public Map<Direction, TrafficLight> getLights() {
        return lights;
    }

    public LightState getLightState(Direction direction) {
        return lights.get(direction).getState();
    }

    public synchronized void changeLightState(Direction direction, LightState newState) {
        if (newState == LightState.GREEN || newState == LightState.YELLOW) {
            validateNoConflictingGreenOrYellow(direction);
        }
        lights.get(direction).changeState(newState);
    }

    private void validateNoConflictingGreenOrYellow(Direction requestedDirection) {
        Set<Direction> conflictingDirections = getConflictingDirections(requestedDirection);
        for (Direction conflictDir : conflictingDirections) {
            LightState state = lights.get(conflictDir).getState();
            if (state == LightState.GREEN || state == LightState.YELLOW) {
                throw new IllegalStateException("Conflicting directions cannot be green or yellow simultaneously.");
            }
        }
    }

    private Set<Direction> getConflictingDirections(Direction direction) {
        switch (direction) {
            case NORTH:
            case SOUTH:
                return Set.of(Direction.EAST, Direction.WEST);
            case EAST:
            case WEST:
                return Set.of(Direction.NORTH, Direction.SOUTH);
            default:
                return Collections.emptySet();
        }
    }
}

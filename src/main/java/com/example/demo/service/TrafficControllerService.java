package com.example.demo.service;

import com.example.demo.domain.Direction;
import com.example.demo.domain.Intersection;
import com.example.demo.domain.LightState;
import com.example.demo.entity.StateChangeHistory;
import com.example.demo.repository.StateChangeHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class TrafficControllerService {

    private final Intersection intersection;
    private final StateChangeHistoryRepository historyRepository;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private boolean paused = false;

    public TrafficControllerService(StateChangeHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
        this.intersection = new Intersection();
        recordInitialStates();
    }

    private void recordInitialStates() {
        for (Direction dir : Direction.values()) {
            recordHistory(dir, LightState.RED);
        }
    }

    public Map<Direction, LightState> getCurrentState() {
        lock.readLock().lock();
        try {
            Map<Direction, LightState> states = new EnumMap<>(Direction.class);
            intersection.getLights().forEach((dir, light) -> states.put(dir, light.getState()));
            return states;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void changeSequence(Direction direction, LightState newState) {
        lock.writeLock().lock();
        try {
            if (paused) {
                throw new IllegalStateException("System is paused. Cannot change light sequences.");
            }
            intersection.changeLightState(direction, newState);
            recordHistory(direction, newState);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void pause() {
        lock.writeLock().lock();
        try {
            this.paused = true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void resume() {
        lock.writeLock().lock();
        try {
            this.paused = false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean isPaused() {
        lock.readLock().lock();
        try {
            return paused;
        } finally {
            lock.readLock().unlock();
        }
    }

    private void recordHistory(Direction direction, LightState state) {
        historyRepository.save(new StateChangeHistory(direction, state, LocalDateTime.now()));
    }

    public List<StateChangeHistory> getHistory() {
        return historyRepository.findAll();
    }
}

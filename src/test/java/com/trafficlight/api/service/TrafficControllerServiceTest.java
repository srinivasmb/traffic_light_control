package com.trafficlight.api.service;

import com.trafficlight.api.domain.Direction;
import com.trafficlight.api.domain.LightState;
import com.trafficlight.api.entity.StateChangeHistory;
import com.trafficlight.api.repository.StateChangeHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrafficControllerServiceTest {

    @Mock
    private StateChangeHistoryRepository historyRepository;

    private TrafficControllerService service;

    @BeforeEach
    void setUp() {
        service = new TrafficControllerService(historyRepository);
    }

    @Test
    void shouldInitializeWithAllRedAndSaveHistory() {
        Map<Direction, LightState> states = service.getCurrentState();
        assertEquals(4, states.size());
        assertEquals(LightState.RED, states.get(Direction.NORTH));

        // It should save 4 history records on init
        verify(historyRepository, times(4)).save(any(StateChangeHistory.class));
    }

    @Test
    void shouldChangeLightSequenceSafelyAndSaveHistory() {
        assertDoesNotThrow(() -> {
            service.changeSequence(Direction.NORTH, LightState.GREEN);
            service.changeSequence(Direction.SOUTH, LightState.GREEN);
        });

        assertEquals(LightState.GREEN, service.getCurrentState().get(Direction.NORTH));

        ArgumentCaptor<StateChangeHistory> captor = ArgumentCaptor.forClass(StateChangeHistory.class);
        verify(historyRepository, atLeast(2)).save(captor.capture());

        List<StateChangeHistory> savedHistory = captor.getAllValues();
        long greenCount = savedHistory.stream()
                .filter(h -> h.getState() == LightState.GREEN && (h.getDirection() == Direction.NORTH || h.getDirection() == Direction.SOUTH))
                .count();

        assertEquals(2, greenCount);
    }

    @Test
    void shouldRejectConflictingCommands() {
        service.changeSequence(Direction.NORTH, LightState.GREEN);

        assertThrows(IllegalStateException.class, () -> {
            service.changeSequence(Direction.EAST, LightState.GREEN);
        });
    }

    @Test
    void shouldPauseAndResumeSystem() {
        assertFalse(service.isPaused());
        service.pause();
        assertTrue(service.isPaused());

        // When paused, changes should be rejected
        assertThrows(IllegalStateException.class, () -> {
            service.changeSequence(Direction.NORTH, LightState.GREEN);
        });

        service.resume();
        assertFalse(service.isPaused());

        assertDoesNotThrow(() -> {
            service.changeSequence(Direction.NORTH, LightState.GREEN);
        });
    }
}

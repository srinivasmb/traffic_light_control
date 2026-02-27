package com.trafficlight.api.controller;

import com.trafficlight.api.domain.Direction;
import com.trafficlight.api.domain.LightState;
import com.trafficlight.api.dto.CommandRequest;
import com.trafficlight.api.dto.HistoryResponse;
import com.trafficlight.api.service.TrafficControllerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/intersections/{id}")
public class TrafficLightController {

    private final TrafficControllerService service;

    public TrafficLightController(TrafficControllerService service) {
        this.service = service;
    }

    @GetMapping("/state")
    public ResponseEntity<Map<Direction, LightState>> getState(@PathVariable String id) {
        // Ignoring ID for now as requirement mentions "possible future expansion"
        return ResponseEntity.ok(service.getCurrentState());
    }

    @GetMapping("/history")
    public ResponseEntity<List<HistoryResponse>> getHistory(@PathVariable String id) {
        List<HistoryResponse> history = service.getHistory().stream()
                .map(h -> new HistoryResponse(h.getDirection(), h.getState(), h.getTimestamp()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(history);
    }

    @PostMapping("/command")
    public ResponseEntity<?> sendCommand(@PathVariable String id, @RequestBody CommandRequest request) {
        try {
            switch (request.getType().toUpperCase()) {
                case "PAUSE":
                    service.pause();
                    return ResponseEntity.ok("System paused.");
                case "RESUME":
                    service.resume();
                    return ResponseEntity.ok("System resumed.");
                case "CHANGE":
                    if (request.getDirection() == null || request.getState() == null) {
                        return ResponseEntity.badRequest().body("Direction and state required for CHANGE command.");
                    }
                    service.changeSequence(request.getDirection(), request.getState());
                    return ResponseEntity.ok("Sequence changed successfully.");
                default:
                    return ResponseEntity.badRequest().body("Unknown command type.");
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

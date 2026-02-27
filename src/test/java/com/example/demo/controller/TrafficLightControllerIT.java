package com.example.demo.controller;

import com.example.demo.domain.Direction;
import com.example.demo.domain.LightState;
import com.example.demo.dto.CommandRequest;
import com.example.demo.service.TrafficControllerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class TrafficLightControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TrafficControllerService service;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        // Reset states for each test since we are not using dirties context to save time
        // The service maintains state in memory for the intersection
        service.resume();
        // we can't easily reset the intersection state explicitly without adding a test-only reset method.
        // But we can pause/resume and test valid sequences.
    }

    @Test
    void shouldReturnInitialState() throws Exception {
        mockMvc.perform(get("/api/v1/intersections/1/state"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.NORTH").exists())
                .andExpect(jsonPath("$.SOUTH").exists())
                .andExpect(jsonPath("$.EAST").exists())
                .andExpect(jsonPath("$.WEST").exists());
    }

    @Test
    void shouldAcceptPauseAndResumeCommand() throws Exception {
        CommandRequest pauseReq = new CommandRequest();
        pauseReq.setType("PAUSE");

        mockMvc.perform(post("/api/v1/intersections/1/command")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pauseReq)))
                .andExpect(status().isOk())
                .andExpect(content().string("System paused."));

        CommandRequest resumeReq = new CommandRequest();
        resumeReq.setType("RESUME");

        mockMvc.perform(post("/api/v1/intersections/1/command")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resumeReq)))
                .andExpect(status().isOk())
                .andExpect(content().string("System resumed."));
    }
}

package com.gts.testgts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gts.testgts.dtos.EventDto;
import com.gts.testgts.repository.EventRepository;
import com.gts.testgts.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EventRepository eventRepository;

    @Test
    public void createEventTest() throws Exception {
        LocalDateTime example = LocalDateTime.of(2023, 8, 14, 20, 1);
        EventDto eventDto = new EventDto(1L, "произошла утечка масла во втором редукторе", example.plusHours(1));
        mockMvc.perform(post("/api/v1/event/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.message").value("произошла утечка масла во втором редукторе"));
    }
}

package com.gts.testgts.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gts.testgts.StubMessageChannel;
import com.gts.testgts.dtos.EventDto;
import com.gts.testgts.dtos.UserDto;
import com.gts.testgts.entity.NotificationPeriod;
import com.gts.testgts.repository.EventRepository;
import com.gts.testgts.repository.NotificationStorage;
import com.gts.testgts.service.schedulers.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    private SimpMessagingTemplate messagingTemplate;
    private StubMessageChannel messageChannel;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    NotificationStorage notificationStorage;
    @Autowired
    NotificationService notificationService;

    @BeforeEach
    public void setup() {
        this.messageChannel = new StubMessageChannel();
        this.messagingTemplate = new SimpMessagingTemplate(this.messageChannel);
        notificationService = new NotificationService(eventRepository, notificationStorage, messagingTemplate);
    }

    @Test
    public void createUserTest() throws Exception {
        LocalDateTime example = LocalDateTime.of(2023, 8, 14, 20, 1);
        NotificationPeriod notificationPeriod = new NotificationPeriod(1L, DayOfWeek.MONDAY, example, example.plusHours(4));
        UserDto userDto = new UserDto(0L, "Василий", "Иванов", "Иванович", List.of(notificationPeriod));
        mockMvc.perform(post("/api/v1/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname").value("Василий"));
    }

    @Test
    public void sendNotificationToUser() throws Exception {
        LocalDateTime example = LocalDateTime.now();
        NotificationPeriod notificationPeriod = new NotificationPeriod(1L, example.getDayOfWeek(), example.minusHours(1),
                example.plusHours(4));
        UserDto userDto = new UserDto(1L, "Иван", "Иванов", "Иванович",
                List.of(notificationPeriod));
        mockMvc.perform(post("/api/v1/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstname").value("Иван"));
        EventDto eventDto = new EventDto(1L, "произошла утечка масла во втором редукторе",
                example);
        mockMvc.perform(post("/api/v1/event/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.message").value("произошла утечка масла во втором редукторе"));

        notificationService.sendNotificationsToUsers();

        List<Message<byte[]>> messages = this.messageChannel.getMessages();

        assertThat(messages).hasSize(1);

        Message<byte[]> message = messages.get(0);
        SimpMessageHeaderAccessor headerAccessor =
                MessageHeaderAccessor.getAccessor(message, SimpMessageHeaderAccessor.class);

        assertThat(headerAccessor).isNotNull();
        assertThat(headerAccessor.getMessageType()).isEqualTo(SimpMessageType.MESSAGE);
        assertThat(headerAccessor.getDestination()).isEqualTo("/user/1/notification");
    }

    @Test
    public void doNotSendNotificationToUser() throws Exception {
        LocalDateTime example = LocalDateTime.now();
        NotificationPeriod notificationPeriod = new NotificationPeriod(1L, example.getDayOfWeek(), example.minusHours(2),
                example.minusHours(1));
        UserDto userDto = new UserDto(1L, "Иван", "Иванов", "Иванович",
                List.of(notificationPeriod));
        mockMvc.perform(post("/api/v1/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstname").value("Иван"));
        EventDto eventDto = new EventDto(1L, "произошла утечка масла во втором редукторе",
                example);
        mockMvc.perform(post("/api/v1/event/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.message").value("произошла утечка масла во втором редукторе"));

        notificationService.sendNotificationsToUsers();

        List<Message<byte[]>> messages = this.messageChannel.getMessages();

        assertThat(messages).hasSize(0);
    }
}

package com.gts.testgts;

import com.gts.testgts.entity.Event;
import com.gts.testgts.entity.NotificationPeriod;
import com.gts.testgts.entity.User;
import com.gts.testgts.repository.EventRepository;
import com.gts.testgts.repository.NotificationStorage;
import com.gts.testgts.service.schedulers.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageSendingTest {

    private SimpMessagingTemplate messagingTemplate;
    private StubMessageChannel messageChannel;
    @Mock
    EventRepository eventRepository;
    @Mock
    NotificationStorage notificationStorage;
    NotificationService notificationService;


    @BeforeEach
    public void setup() {
        this.messageChannel = new StubMessageChannel();
        this.messagingTemplate = new SimpMessagingTemplate(this.messageChannel);
        notificationService = new NotificationService(eventRepository, notificationStorage, messagingTemplate);
    }

    @Test
    public void sendNotificationToUser() {
        LocalDateTime example = LocalDateTime.of(2023, 8, 14, 20, 1);
        NotificationPeriod notificationPeriod = new NotificationPeriod(1L, DayOfWeek.MONDAY, example,
                example.plusHours(4));
        User user = new User(1L, "Иван", "Иванов", "Иванович", List.of(notificationPeriod));
        Event event = new Event(1L, "произошла утечка масла во втором редукторе", example.plusHours(1),
                List.of());
        when(eventRepository.findAll()).thenReturn(List.of(event));
        when(notificationStorage.findUsersToSendNotificationOnEventId(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(List.of(user));
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
    public void doNotSendNotificationToUser() {
        LocalDateTime example = LocalDateTime.of(2023, 8, 14, 20, 1);
        NotificationPeriod notificationPeriod = new NotificationPeriod(1L, DayOfWeek.MONDAY, example,
                example.plusHours(1));
        Event event = new Event(1L, "произошла утечка масла во втором редукторе", example.plusHours(3),
                List.of());
        when(eventRepository.findAll()).thenReturn(List.of(event));
        when(notificationStorage.findUsersToSendNotificationOnEventId(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(List.of());
        notificationService.sendNotificationsToUsers();

        List<Message<byte[]>> messages = this.messageChannel.getMessages();

        assertThat(messages).hasSize(0);
    }
}
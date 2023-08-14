package com.gts.testgts.service.schedulers;

import com.gts.testgts.entity.Event;
import com.gts.testgts.entity.User;
import com.gts.testgts.repository.EventRepository;
import com.gts.testgts.repository.NotificationStorage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {

    EventRepository eventRepository;
    NotificationStorage notificationStorage;
    SimpMessagingTemplate template;
    DateTimeFormatter logFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    @Scheduled(cron = "*/30 * * * * *")
    public void sendNotificationsToUsers() {
        log.info("Start run tasks");
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        List<Event> eventsToSend = eventRepository.findAll();
        for (Event toSend : eventsToSend) {
            List<User> usersToSend = notificationStorage.findUsersToSendNotificationOnEventId(dayOfWeek, now, toSend.getId());
            List<User> usersSent = new ArrayList<>(toSend.getSentTo());
            for (User user : usersToSend) {
                template.convertAndSendToUser(String.valueOf(user.getId()), "/notification", toSend);
                log.info(now.format(logFormatter) + " Пользователю " + user.getLastname() + " " + user.getFirstname() + " "
                        + user.getSurname() + " отправлено оповещение с текстом: " + toSend.getMessage());
                usersSent.add(user);
            }
            toSend.setSentTo(usersSent);
        }
        eventRepository.saveAll(eventsToSend);
    }
}

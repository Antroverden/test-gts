package com.gts.testgts.repository;

import com.gts.testgts.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Primary
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationStorage {

    JdbcTemplate jdbcTemplate;

    public List<User> findUsersToSendNotificationOnEventId(DayOfWeek dayOfWeek, LocalDateTime now, Long eventId) {
        String sqlQuery = "SELECT * FROM users WHERE id in (SELECT user_id from notification_periods where " +
                "day_of_week = ? AND time_from < ? AND time_to > ?) and id not in (SELECT user_id from events right join " +
                "event_to_user etu on events.id = etu.event_id WHERE events.id = ?)";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUsers, dayOfWeek.name(), now, now, eventId);
    }

    private User mapRowToUsers(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .firstname(resultSet.getString("firstname"))
                .lastname(resultSet.getString("lastname"))
                .surname(resultSet.getString("surname"))
                .build();
    }
}

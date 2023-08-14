package com.gts.testgts.dtos;

import com.gts.testgts.entity.NotificationPeriod;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    Long id;
    String firstname;
    String lastname;
    String surname;
    List<NotificationPeriod> notificationPeriods;
}

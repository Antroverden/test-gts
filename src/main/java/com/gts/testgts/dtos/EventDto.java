package com.gts.testgts.dtos;

import com.gts.testgts.entity.Event;
import com.gts.testgts.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    Long id;
    String message;
    LocalDateTime happenAt;
}

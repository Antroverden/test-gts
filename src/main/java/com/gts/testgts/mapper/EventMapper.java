package com.gts.testgts.mapper;

import com.gts.testgts.dtos.EventDto;
import com.gts.testgts.entity.Event;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface EventMapper {

    Event toEvent(EventDto dto);

    EventDto toEventDto(Event event);

    List<EventDto> toEventDtos(List<Event> events);
}

package com.gts.testgts.mapper;

import com.gts.testgts.dtos.EventDto;
import com.gts.testgts.entity.Event;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    Event toEvent(EventDto dto);

    EventDto toEventDto(Event event);

    List<EventDto> toEventDtos(List<Event> events);
}

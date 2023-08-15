package com.gts.testgts.service;

import com.gts.testgts.dtos.EventDto;
import com.gts.testgts.entity.Event;
import com.gts.testgts.exception.NotFoundException;
import com.gts.testgts.mapper.EventMapper;
import com.gts.testgts.repository.EventRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventService {

    EventRepository eventRepository;
    EventMapper eventMapper;

    public EventDto create(EventDto EventDto) {
        Event event = eventMapper.toEvent(EventDto);
        event.setSentTo(List.of());
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toEventDto(savedEvent);
    }

    public EventDto update(EventDto EventDto) {
        Event event = eventMapper.toEvent(EventDto);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toEventDto(savedEvent);
    }

    public EventDto getById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Событие не найдено"));
        return eventMapper.toEventDto(event);
    }

    public List<EventDto> getAll() {
        List<Event> events = eventRepository.findAll();
        return eventMapper.toEventDtos(events);
    }

    public void deleteUser(Long eventId) {
        if (!eventRepository.existsById(eventId)) throw new NotFoundException("Событие не найдено");
        eventRepository.deleteById(eventId);
    }
}

package com.gts.testgts.controller;

import com.gts.testgts.dtos.EventDto;
import com.gts.testgts.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Контроллер управления событиями")
public class EventController {

    EventService eventService;

    @PostMapping
    @Operation(summary = "Создание события")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto create(@RequestBody EventDto EventDto) {
        return eventService.create(EventDto);
    }

    @PutMapping
    @Operation(summary = "Изменение события")
    public EventDto update(@RequestBody EventDto EventDto) {
        return eventService.update(EventDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить событие по id")
    public EventDto getById(@PathVariable Long id) {
        return eventService.getById(id);
    }

    @GetMapping
    @Operation(summary = "Получить все события")
    public List<EventDto> getAll() {
        return eventService.getAll();
    }

    @DeleteMapping("/{eventId}")
    @Operation(summary = "Удалить событие по id")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long eventId) {
        eventService.deleteUser(eventId);
    }
}

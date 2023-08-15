package com.gts.testgts.controller;

import com.gts.testgts.dtos.UserDto;
import com.gts.testgts.service.UserService;
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
@RequestMapping("/api/v1/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Контроллер управления пользователями")
public class UserController {

    UserService userService;

    @PostMapping
    @Operation(summary = "Создание пользователя")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody UserDto UserDto) {
        return userService.create(UserDto);
    }

    @PutMapping
    @Operation(summary = "Изменение пользователя")
    public UserDto update(@RequestBody UserDto UserDto) {
        return userService.update(UserDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по id")
    public UserDto getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping
    @Operation(summary = "Получить всех пользователей")
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Удалить пользователя по id")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}

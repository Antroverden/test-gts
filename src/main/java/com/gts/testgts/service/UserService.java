package com.gts.testgts.service;

import com.gts.testgts.dtos.UserDto;
import com.gts.testgts.entity.User;
import com.gts.testgts.exception.NotFoundException;
import com.gts.testgts.mapper.UserMapper;
import com.gts.testgts.repository.NotificationPeriodRepository;
import com.gts.testgts.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    NotificationPeriodRepository notificationPeriodRepository;

    public UserDto create(UserDto UserDto) {
        User user = userMapper.toUser(UserDto);
        notificationPeriodRepository.saveAll(user.getNotificationPeriods());
        User savedUser = userRepository.save(user);
        return userMapper.toUserDto(savedUser);
    }

    public UserDto update(UserDto UserDto) {
        User user = userMapper.toUser(UserDto);
        User savedUser = userRepository.save(user);
        return userMapper.toUserDto(savedUser);
    }

    public UserDto getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return userMapper.toUserDto(user);
    }

    public List<UserDto> getAll() {
        List<User> users = userRepository.findAll();
        return userMapper.toUserDtos(users);
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) throw new NotFoundException("Пользователь не найден");
        userRepository.deleteById(userId);
    }
}

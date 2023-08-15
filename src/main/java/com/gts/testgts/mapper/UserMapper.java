package com.gts.testgts.mapper;

import com.gts.testgts.dtos.UserDto;
import com.gts.testgts.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserDto dto);

    UserDto toUserDto(User user);

    List<UserDto> toUserDtos(List<User> users);
}

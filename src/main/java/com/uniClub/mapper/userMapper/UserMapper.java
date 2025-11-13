package com.uniClub.mapper.userMapper;

import com.uniClub.dto.userDto.UserDto;
import com.uniClub.entity.userEntity.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {

    public static UserEntity toEntity(UserDto request, PasswordEncoder passwordEncoder) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setRole(request.getRole());
        return userEntity;
    }
    public static UserDto toDto(UserEntity entity) {
        UserDto userDto = new UserDto();
        userDto.setUsername(entity.getUsername());
        userDto.setRole(entity.getRole());
        return userDto;

    }
}

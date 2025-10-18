package com.uniClub.user.internal.mapper;

import com.uniClub.user.api.dto.AuthRequest;
import com.uniClub.user.api.dto.UserDto;
import com.uniClub.user.internal.entity.UserEntity;
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

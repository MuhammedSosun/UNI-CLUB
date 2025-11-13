package com.uniClub.mapper.userMapper;

import com.uniClub.dto.userDto.UserDto;
import com.uniClub.entity.userEntity.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {

    public static UserEntity toEntity(UserDto request, PasswordEncoder passwordEncoder) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(request.getUsername());
        userEntity.setEmail(request.getEmail());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setRole(request.getRole());
        userEntity.setCreatedBy(request.getCreatedBy());
        userEntity.setUpdatedBy(request.getUpdatedBy());
        userEntity.setCreatedAt(request.getCreatedAt());
        userEntity.setUpdatedAt(request.getUpdatedAt());
        return userEntity;
    }
    public static UserDto toDto(UserEntity entity) {
        UserDto userDto = new UserDto();
        userDto.setId(entity.getId());
        userDto.setUsername(entity.getUsername());
        userDto.setEmail(entity.getEmail());
        userDto.setRole(entity.getRole());
        userDto.setPassword(entity.getPassword());
        userDto.setCreatedBy(entity.getCreatedBy());
        userDto.setUpdatedBy(entity.getUpdatedBy());
        userDto.setCreatedAt(entity.getCreatedAt());
        userDto.setUpdatedAt(entity.getUpdatedAt());
        return userDto;
    }

}

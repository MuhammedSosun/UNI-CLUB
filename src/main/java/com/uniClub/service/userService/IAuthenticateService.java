package com.uniClub.service.userService;

import com.uniClub.dto.userDto.AuthRequest;
import com.uniClub.dto.userDto.AuthResponse;
import com.uniClub.dto.userDto.RefreshTokenRequest;
import com.uniClub.dto.userDto.UserDto;

public interface IAuthenticateService {
    UserDto register(UserDto userDto);
    AuthResponse authenticate(AuthRequest authRequest);
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    void logout();
}

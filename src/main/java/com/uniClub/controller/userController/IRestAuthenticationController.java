package com.uniClub.controller.userController;

import com.uniClub.controller.controller.RootEntity;
import com.uniClub.dto.userDto.AuthRequest;
import com.uniClub.dto.userDto.AuthResponse;
import com.uniClub.dto.userDto.RefreshTokenRequest;
import com.uniClub.dto.userDto.UserDto;
import org.springframework.http.ResponseEntity;

public interface IRestAuthenticationController {

    RootEntity<UserDto> register(UserDto userDto);
    RootEntity<AuthResponse> authenticate(AuthRequest request);
    RootEntity<AuthResponse> refreshToken(RefreshTokenRequest refreshTokenRequest);
    ResponseEntity<Void> logout();
}

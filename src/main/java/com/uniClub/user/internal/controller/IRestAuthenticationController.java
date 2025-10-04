package com.uniClub.user.internal.controller;

import com.uniClub.common.controller.RootEntity;
import com.uniClub.user.api.dto.AuthRequest;
import com.uniClub.user.api.dto.AuthResponse;
import com.uniClub.user.api.dto.RefreshTokenRequest;
import com.uniClub.user.api.dto.UserDto;

public interface IRestAuthenticationController {

    RootEntity<UserDto> register(UserDto userDto);
    RootEntity<AuthResponse> authenticate(AuthRequest request);
    RootEntity<AuthResponse> refreshToken(RefreshTokenRequest refreshTokenRequest);
}

package com.uniClub.user.internal.service;

import com.uniClub.user.api.dto.*;

public interface IAuthenticateService {
    UserDto register(UserDto userDto);
    AuthResponse authenticate(AuthRequest authRequest);
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}

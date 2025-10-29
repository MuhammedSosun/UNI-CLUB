package com.uniClub.user.internal.service;

import com.uniClub.user.api.dto.*;
import com.uniClub.user.api.enums.Role;

import java.util.List;
import java.util.UUID;

public interface IAuthenticateService {
    public AuthResponse register(RegisterRequest request);
    AuthResponse authenticate(AuthRequest authRequest);
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    UserDto updateUserRole(UUID userId, Role newRole);
    List<UserDto> allUsers();
    List<UserDto> searchUsers(String filter);
    void logout();
}

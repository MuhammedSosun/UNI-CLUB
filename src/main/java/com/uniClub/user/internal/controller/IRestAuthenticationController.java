package com.uniClub.user.internal.controller;

import com.uniClub.common.controller.RootEntity;
import com.uniClub.user.api.dto.*;
import com.uniClub.user.api.enums.Role;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface IRestAuthenticationController {

    RootEntity<?> register(RegisterRequest request);
    RootEntity<AuthResponse> authenticate(AuthRequest request);
    RootEntity<AuthResponse> refreshToken(RefreshTokenRequest refreshTokenRequest);
    RootEntity<UserDto> updateUserRole(UUID userId, Role newRole);
    RootEntity<List<UserDto>> allUsers();
    RootEntity<List<UserDto>> searchUsers(String filter);
    ResponseEntity<Void> logout();
}

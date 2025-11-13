package com.uniClub.controller.userController;

import com.uniClub.controller.controller.RootEntity;
import com.uniClub.dto.userDto.*;
import com.uniClub.enums.Role;
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

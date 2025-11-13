package com.uniClub.service.userService;

import com.uniClub.dto.userDto.*;
import com.uniClub.enums.Role;

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

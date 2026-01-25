package com.uniClub.user.userController;

import com.uniClub.util.controller.RootEntity;
import com.uniClub.enums.Role;
import com.uniClub.user.userDto.*;
import com.uniClub.util.pageable.PageableEntity;
import com.uniClub.util.pageable.PageableRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface IRestAuthenticationController {

    RootEntity<?> register(RegisterRequest request);
    RootEntity<AuthResponse> authenticate(AuthRequest request);
    RootEntity<AuthResponse> refreshToken(RefreshTokenRequest refreshTokenRequest);
    RootEntity<PageableEntity<UserDto>> getUsersPaged(PageableRequest pageableRequest);
    RootEntity<UserDto> updateUserRole(UUID userId, Role newRole);
    RootEntity<List<UserDto>> allUsers();
    RootEntity<List<UserDto>> searchUsers(String filter);
    ResponseEntity<Void> logout(Authentication authentication);
}

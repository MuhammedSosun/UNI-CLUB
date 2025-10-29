package com.uniClub.user.internal.controller.impl;

import com.uniClub.common.controller.RestBaseController;
import com.uniClub.common.controller.RootEntity;
import com.uniClub.user.api.dto.*;
import com.uniClub.user.api.enums.Role;
import com.uniClub.user.internal.controller.IRestAuthenticationController;
import com.uniClub.user.internal.service.IAuthenticateService;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")

public class RestAuthenticationControllerImpl extends RestBaseController implements IRestAuthenticationController {

    private final IAuthenticateService authenticateService;

    public RestAuthenticationControllerImpl(IAuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @PostMapping("/register")
    @Override
    public RootEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return ok(authenticateService.register(request));
    }
    @PostMapping("/authenticate")
    @Override
    public RootEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        return ok(authenticateService.authenticate(request));
    }

    @PostMapping("/refreshToken")
    @Override
    public RootEntity<AuthResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return ok(authenticateService.refreshToken(refreshTokenRequest));
    }
    @PutMapping("/users/{userId}/role")
    @Override
    public RootEntity<UserDto> updateUserRole(@PathVariable UUID userId,@RequestParam Role newRole) {
        return ok(authenticateService.updateUserRole(userId, newRole));
    }
    @GetMapping("/all/users")
    @Override
    public RootEntity<List<UserDto>> allUsers() {
        return ok(authenticateService.allUsers());
    }
    @GetMapping("/filter/users")
    @Override
    public RootEntity<List<UserDto>> searchUsers(@RequestParam(required = false) String filter) {
        return ok(authenticateService.searchUsers(filter));
    }

    @PostMapping("/logout")
    @Override
    public ResponseEntity<Void> logout() {
        authenticateService.logout();
        return ResponseEntity.ok().build();

    }
}

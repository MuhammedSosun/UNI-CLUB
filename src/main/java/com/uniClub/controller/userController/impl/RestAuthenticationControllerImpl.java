package com.uniClub.controller.userController.impl;

import com.uniClub.controller.controller.RestBaseController;
import com.uniClub.controller.controller.RootEntity;
import com.uniClub.dto.userDto.AuthRequest;
import com.uniClub.dto.userDto.AuthResponse;
import com.uniClub.dto.userDto.RefreshTokenRequest;
import com.uniClub.dto.userDto.UserDto;
import com.uniClub.controller.userController.IRestAuthenticationController;
import com.uniClub.service.userService.IAuthenticateService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

public class RestAuthenticationControllerImpl extends RestBaseController implements IRestAuthenticationController {

    private final IAuthenticateService authenticateService;

    public RestAuthenticationControllerImpl(IAuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @PostMapping("/register")
    @Override
    public RootEntity<UserDto> register(@Valid @RequestBody UserDto userDto) {
        return ok(authenticateService.register(userDto));
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
    @PostMapping("/logout")
    @Override
    public ResponseEntity<Void> logout() {
        authenticateService.logout();
        return ResponseEntity.ok().build();

    }
}

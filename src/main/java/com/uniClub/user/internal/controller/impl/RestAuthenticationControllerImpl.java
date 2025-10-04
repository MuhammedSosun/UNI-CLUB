package com.uniClub.user.internal.controller.impl;

import com.uniClub.common.controller.RestBaseController;
import com.uniClub.common.controller.RootEntity;
import com.uniClub.user.api.dto.AuthRequest;
import com.uniClub.user.api.dto.AuthResponse;
import com.uniClub.user.api.dto.RefreshTokenRequest;
import com.uniClub.user.api.dto.UserDto;
import com.uniClub.user.internal.controller.IRestAuthenticationController;
import com.uniClub.user.internal.service.IAuthenticateService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
    public RootEntity<AuthResponse> authenticate(AuthRequest request) {
        return ok(authenticateService.authenticate(request));
    }

    @PostMapping("/refreshToken")
    @Override
    public RootEntity<AuthResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return ok(authenticateService.refreshToken(refreshTokenRequest));
    }
}

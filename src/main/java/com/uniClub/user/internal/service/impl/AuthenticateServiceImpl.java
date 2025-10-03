package com.uniClub.user.internal.service.impl;

import com.uniClub.user.api.dto.*;
import com.uniClub.user.internal.entity.RefreshToken;
import com.uniClub.user.internal.entity.UserEntity;
import com.uniClub.user.internal.service.IAuthenticateService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class AuthenticateServiceImpl implements IAuthenticateService {



    private RefreshToken createRefreshToken(UserEntity user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiredDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *4));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());

        return refreshToken;
    }
    @Override
    public UserDto register(AuthRequest authRequest) {
        return null;
    }

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        return null;
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return null;
    }
}

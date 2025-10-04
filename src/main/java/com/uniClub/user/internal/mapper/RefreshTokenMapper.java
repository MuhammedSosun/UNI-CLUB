package com.uniClub.user.internal.mapper;

import com.uniClub.user.internal.entity.RefreshToken;
import com.uniClub.user.internal.entity.UserEntity;

import java.util.Date;
import java.util.UUID;

public class RefreshTokenMapper {

    public static RefreshToken generate(UserEntity user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiredDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *2));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        return refreshToken;
    }
}

package com.uniClub.user.userMapper;

import com.uniClub.user.userEntity.RefreshToken;
import com.uniClub.user.userEntity.UserEntity;

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

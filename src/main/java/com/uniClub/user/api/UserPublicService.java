package com.uniClub.user.api;

import com.uniClub.user.internal.entity.UserEntity;

import java.util.Optional;

public interface UserPublicService {

    Optional<UserEntity> findUserByUsername(String username);
}

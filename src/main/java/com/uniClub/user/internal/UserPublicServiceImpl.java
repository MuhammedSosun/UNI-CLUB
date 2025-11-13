package com.uniClub.user.internal;

import com.uniClub.common.exceptions.exception.BaseException;
import com.uniClub.common.exceptions.exception.ErrorMessage;
import com.uniClub.common.exceptions.exception.MessageType;
import com.uniClub.user.api.UserPublicService;
import com.uniClub.user.internal.entity.UserEntity;
import com.uniClub.user.internal.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserPublicServiceImpl implements UserPublicService {

    private final UserRepository userRepository;

    public UserPublicServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UUID getUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserEntity::getId)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.USER_NOT_FOUND, username)));
    }

}

package com.uniClub.user.internal;

import com.uniClub.user.api.UserPublicService;
import com.uniClub.user.internal.entity.UserEntity;
import com.uniClub.user.internal.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPublicServiceImpl implements UserPublicService {

    private final UserRepository userRepository;

    public UserPublicServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserEntity> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

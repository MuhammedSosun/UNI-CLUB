package com.uniClub.user.internal.service.impl;

import com.uniClub.common.exceptions.exception.BaseException;
import com.uniClub.common.exceptions.exception.ErrorMessage;
import com.uniClub.common.exceptions.exception.MessageType;
import com.uniClub.common.logging.LoggableOperation;
import com.uniClub.common.utils.OperationType;
import com.uniClub.security.JwtService;
import com.uniClub.user.api.dto.*;
import com.uniClub.user.internal.entity.RefreshToken;
import com.uniClub.user.internal.entity.UserEntity;
import com.uniClub.user.internal.mapper.RefreshTokenMapper;
import com.uniClub.user.internal.mapper.UserMapper;
import com.uniClub.user.internal.repository.RefreshTokenRepository;
import com.uniClub.user.internal.repository.UserRepository;
import com.uniClub.user.internal.service.IAuthenticateService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
public class AuthenticateServiceImpl implements IAuthenticateService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthenticateServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, AuthenticationProvider authenticationProvider, JwtService jwtService, RefreshTokenRepository refreshTokenRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.authenticationProvider = authenticationProvider;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
    }
    private RefreshToken createRefreshToken(UserEntity user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiredDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *4));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());

        return refreshToken;
    }
    @Transactional
    @LoggableOperation(OperationType.REGISTER)
    @Override
    public UserDto register(UserDto userDto) {
        UserEntity user = UserMapper.toEntity(userDto,bCryptPasswordEncoder);
        UserEntity savedUser = userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }
    @Transactional
    @LoggableOperation(OperationType.LOGIN)
    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {

        try {
            authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            UserEntity user = userRepository.findByUsername(authRequest.getUsername())
                    .orElseThrow(()-> new UsernameNotFoundException("Username not found"));
            String accessToken = jwtService.generateToken(user);
            RefreshToken refreshToken = refreshTokenRepository.save(RefreshTokenMapper.generate(user));
            return new AuthResponse(accessToken,refreshToken.getRefreshToken());
        }catch (Exception e) {
            throw new UsernameNotFoundException("Invalid username or password");
        }

    }
    @Transactional
    @LoggableOperation(OperationType.REFRESH_TOKEN)
    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenRequest.getRefreshToken())
                .orElseThrow(()-> new UsernameNotFoundException("Refresh token not found"));
        if (!isValid(refreshToken.getExpiredDate())){
            throw new UsernameNotFoundException("Invalid refresh token");
        }
        UserEntity user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(user);
        RefreshToken newRefreshToken = refreshTokenRepository.save(RefreshTokenMapper.generate(user));

        return new AuthResponse(accessToken,newRefreshToken.getRefreshToken());
    }
    @Transactional
    @Override
    public void logout() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                ()-> new BaseException(new ErrorMessage(MessageType.USERNAME_NOT_FOUND, username)));
        refreshTokenRepository.deleteAllByUserId(user.getId());
    }

    private boolean isValid(Date expiredDate){
        return expiredDate.after(new Date());
    }
}

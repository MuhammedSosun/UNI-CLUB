package com.uniClub.user.internal.service.impl;

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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    @Override
    public UserDto register(UserDto userDto) {
        UserEntity user = UserMapper.toEntity(userDto,bCryptPasswordEncoder);
        UserEntity savedUser = userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }

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
    private boolean isValid(Date expiredDate){
        return expiredDate.after(new Date());
    }
}

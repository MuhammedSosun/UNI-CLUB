package com.uniClub.service.userService.impl;

import com.uniClub.exceptions.exception.BaseException;
import com.uniClub.exceptions.exception.ErrorMessage;
import com.uniClub.exceptions.exception.MessageType;
import com.uniClub.logging.LoggableOperation;
import com.uniClub.enums.OperationType;
import com.uniClub.dto.userDto.*;
import com.uniClub.security.JwtService;
import com.uniClub.enums.Role;
import com.uniClub.entity.userEntity.RefreshToken;
import com.uniClub.entity.userEntity.UserEntity;
import com.uniClub.mapper.userMapper.RefreshTokenMapper;
import com.uniClub.mapper.userMapper.UserMapper;
import com.uniClub.repository.userRepository.RefreshTokenRepository;
import com.uniClub.repository.userRepository.UserRepository;
import com.uniClub.service.userService.IAuthenticateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
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
    public AuthResponse register(RegisterRequest request) {
        String email = request.getEmail().trim();
        String username = email.split("@")[0];

        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setUsername(username);
        userDto.setRole(Role.USER);

        log.info("REGISTER REQUEST -> " + request.getEmail() + " / " + request.getPassword());
        UserEntity user = UserMapper.toEntity(userDto, bCryptPasswordEncoder);
        UserEntity savedUser = userRepository.save(user);

        String accessToken = jwtService.generateToken(savedUser);
        RefreshToken refreshToken = refreshTokenRepository.save(RefreshTokenMapper.generate(savedUser));

        return new AuthResponse(accessToken, refreshToken.getRefreshToken());
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
    public UserDto updateUserRole(UUID userId, Role newRole) {
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new BaseException(new ErrorMessage(MessageType.USERNAME_NOT_FOUND, "Username not found"))
        );
        user.setRole(newRole);
        userRepository.save(user);
        return UserMapper.toDto(user);
    }

    @Override
    public List<UserDto> allUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(UserMapper::toDto).toList();
    }

    @Override
    public List<UserDto> searchUsers(String filter) {
        if (filter == null || filter.isBlank()) {
            return userRepository.findAll()
                    .stream()
                    .map(UserMapper::toDto)
                    .toList();
        }
        List<UserEntity> users = userRepository.searchUsers(filter);
        return users.stream()
                .map(UserMapper::toDto)
                .toList();
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

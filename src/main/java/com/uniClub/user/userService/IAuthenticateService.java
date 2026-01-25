package com.uniClub.service.userService;

import com.uniClub.dto.mailDto.VerifyCodeRequest;
import com.uniClub.dto.userDto.*;
import com.uniClub.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IAuthenticateService {
    public String register(RegisterRequest request);
    String verifyCode(VerifyCodeRequest request);
    AuthResponse authenticate(AuthRequest authRequest);
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    UserDto updateUserRole(UUID userId, Role newRole);
    List<UserDto> allUsers();
    Page<UserDto> getUsersPaged(Pageable pageable, String filter);
    List<UserDto> searchUsers(String filter);
    void logout(String username);
}

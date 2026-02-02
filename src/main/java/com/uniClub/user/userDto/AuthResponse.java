package com.uniClub.user.userDto;

import com.uniClub.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String accessToken;

    private String refreshToken;

    // 👇 YENİ EKLENEN ALANLAR
    private UUID userId;

    private String username;

    private Role role;
}
package com.uniClub.dto.mailDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetCodeRequest {

    private String email;

    private String code;

    private String newPassword;

    private String confirmNewPassword;
}

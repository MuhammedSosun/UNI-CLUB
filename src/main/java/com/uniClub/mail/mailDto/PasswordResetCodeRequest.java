package com.uniClub.mail.mailDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "Şifre boş olamaz")
    @Pattern(
            regexp = "^(?=.*[A-Za-z]).{6,}$",
            message = "Şifre en az 6 karakter olmalı ve en az 1 harf içermelidir"
    )
    private String newPassword;

    @NotBlank(message = "Şifre tekrarı boş olamaz")
    private String confirmNewPassword;
}

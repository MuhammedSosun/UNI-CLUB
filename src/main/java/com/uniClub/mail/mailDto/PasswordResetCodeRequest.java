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
    @NotBlank(message = "Email cannot be blank")
    @Pattern(
            regexp = "^[0-9]{9}@ogrenci\\.yalova\\.edu.tr$",
            message = "Geçerli bir üniversite e-postası giriniz! (ör: 210101068@ogrenci.yalova.edu.tr)"
    )
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

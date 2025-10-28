package com.uniClub.user.api.dto;

import com.uniClub.common.baseEntity.DtoBase;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Kullanıcı kayıt isteği")
public class RegisterRequest extends DtoBase {

    @Schema(
            description = "Üniversite e-posta adresi",
            example = "210101068@ogrenci.yalova.edu.tr",
            required = true
    )
    @NotBlank(message = "Email cannot be blank")
    @Pattern(
            regexp = "^[0-9]{9}@ogrenci\\.yalova\\.edu.tr$",
            message = "Geçerli bir üniversite e-postası giriniz! (ör: 210101068@ogrenci.yalova.edu.tr)"
    )
    private String email;

    @Schema(
            description = "Kullanıcı şifresi",
            example = "123456",
            required = true
    )
    @NotBlank(message = "Şifre Boş olamaz")
    private String password;
}
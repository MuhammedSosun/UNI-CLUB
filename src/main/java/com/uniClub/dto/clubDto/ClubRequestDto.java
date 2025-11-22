package com.uniClub.dto.clubDto;

import com.uniClub.enums.StatusEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubRequestDto {

    @NotBlank(message = "Kulüp adı boş olamaz")
    @Size(min = 3, max = 150, message = "Kulüp adı 3-150 karakter arası olmalıdır")
    private String clubName;
    @NotBlank(message = "shortName boş olamaz")
    @Size(min = 2,max = 20,message = "Short name 2-20 karakter arası olmalıdır")
    private String shortName;
    @Size(max = 500, message = "Açıklama en fazla 500 karakter olabilir")
    private String description;
    @Pattern(
            regexp = "^(https?|ftp)://.*$",
            message = "Logo URL geçerli bir link olmalıdır"
    )
    private String logoUrl;
    @Past(message = "Kuruluş tarihi geçmiş bir tarih olmalıdır")
    private LocalDate foundationDate;
    @Email(message = "Geçerli bir email giriniz")
    private String email;
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Telefon numarası 10-15 haneli olmalıdır")
    private String phone;
    @Pattern(
            regexp = "^(https?://)?(www\\.)?instagram\\.com/.*$",
            message = "Instagram linki geçersiz"
    )
    private String instagram;

    private UUID presidentId;
    @NotNull(message = "Onay bilgisi boş olamaz")
    private Boolean approved;
    @NotNull(message = "Status bilgisi boş olamaz")
    private StatusEnum status;
}

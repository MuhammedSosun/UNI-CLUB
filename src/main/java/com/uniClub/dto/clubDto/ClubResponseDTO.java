package com.uniClub.dto.clubDto;

import com.uniClub.enums.StatusEnum;
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
public class ClubResponseDTO {
    private Long id;
    private String clubName;
    private String shortName;
    private String logoUrl;
    private LocalDate foundationDate;
    private String email;
    private String phone;
    private String instagram;
    private String approved;

    private UUID presidentId;

    private String presidentUsername;

    private LocalDate createdAt;

    private StatusEnum status;
}

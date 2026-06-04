package com.uniClub.ai.aiDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiClubPayload {

    private Long clubId;

    private String clubName;
    private String shortName;
    private String description;

    private LocalDate foundationDate;

    private Boolean approved;
    private String status;
}
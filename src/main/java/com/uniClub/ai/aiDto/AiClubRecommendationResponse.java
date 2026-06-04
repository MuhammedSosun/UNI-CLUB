package com.uniClub.ai.aiDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiClubRecommendationResponse {

    private Long clubId;
    private String clubName;
    private Integer matchScore;
    private String reason;
    private List<String> matchedAreas;
}
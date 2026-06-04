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
public class AiRecommendationResponse {

    private String profileSummary;

    private List<AiClubRecommendationResponse> recommendedClubs;

    private List<AiEventRecommendationResponse> recommendedEvents;

    private List<String> developmentSuggestions;
}
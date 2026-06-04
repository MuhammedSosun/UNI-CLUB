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
public class AiRecommendationPayload {

    private AiMemberProfilePayload memberProfile;

    private List<AiClubPayload> availableClubs;

    private List<AiEventPayload> availableEvents;
}
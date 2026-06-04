package com.uniClub.ai.aiController;

import com.uniClub.ai.aiDto.AiRecommendationResponse;
import com.uniClub.ai.aiService.AiRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.PostConstruct;
@RestController
@RequestMapping("/api/ai/recommendations")
@RequiredArgsConstructor
public class AiRecommendationController {

    private final AiRecommendationService aiRecommendationService;

    @PostMapping("/me")
    public ResponseEntity<AiRecommendationResponse> recommendForCurrentMember() {
        AiRecommendationResponse response =
                aiRecommendationService.generateRecommendationForCurrentMember();

        return ResponseEntity.ok(response);
    }

}

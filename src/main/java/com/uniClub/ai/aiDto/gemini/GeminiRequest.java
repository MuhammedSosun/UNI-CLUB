package com.uniClub.ai.aiDto.gemini;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeminiRequest {

    private List<GeminiContent> contents;

    private Map<String, Object> generationConfig;
}
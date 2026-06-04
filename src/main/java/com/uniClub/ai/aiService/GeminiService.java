package com.uniClub.ai.aiService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniClub.ai.aiDto.AiRecommendationPayload;
import com.uniClub.ai.aiDto.AiRecommendationResponse;
import com.uniClub.ai.aiDto.gemini.GeminiContent;
import com.uniClub.ai.aiDto.gemini.GeminiPart;
import com.uniClub.ai.aiDto.gemini.GeminiRequest;
import com.uniClub.ai.aiDto.gemini.GeminiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private static final double TEMPERATURE = 0.25;
    private static final int REQUEST_TIMEOUT_SECONDS = 45;

    private final ObjectMapper objectMapper;
    private final WebClient.Builder webClientBuilder;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model}")
    private String model;

    @Value("${gemini.base-url}")
    private String baseUrl;

    public AiRecommendationResponse generateRecommendation(AiRecommendationPayload payload) {
        validateConfiguration();

        try {
            String inputJson = objectMapper.writeValueAsString(payload);
            String prompt = buildPrompt(inputJson);

            GeminiRequest request = buildGeminiRequest(prompt);

            GeminiResponse geminiResponse = callGemini(request);

            String responseText = extractText(geminiResponse);

            return objectMapper.readValue(responseText, AiRecommendationResponse.class);

        } catch (JsonProcessingException exception) {
            throw new RuntimeException("AI response could not be parsed as AiRecommendationResponse JSON.", exception);
        } catch (Exception exception) {
            throw new RuntimeException("Gemini recommendation request failed.", exception);
        }
    }

    private void validateConfiguration() {
        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("Gemini API key is missing. Please set GEMINI_API_KEY.");
        }

        if (model == null || model.isBlank()) {
            throw new RuntimeException("Gemini model is missing. Please set gemini.model.");
        }

        if (baseUrl == null || baseUrl.isBlank()) {
            throw new RuntimeException("Gemini base URL is missing. Please set gemini.base-url.");
        }
    }

    private GeminiRequest buildGeminiRequest(String prompt) {
        return new GeminiRequest(
                List.of(
                        new GeminiContent(
                                List.of(new GeminiPart(prompt))
                        )
                ),
                Map.of(
                        "temperature", TEMPERATURE,
                        "responseMimeType", "application/json"
                )
        );
    }

    private GeminiResponse callGemini(GeminiRequest request) {
        return webClientBuilder
                .baseUrl(baseUrl)
                .build()
                .post()
                .uri("/models/{model}:generateContent?key={apiKey}", model, apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GeminiResponse.class)
                .block(Duration.ofSeconds(REQUEST_TIMEOUT_SECONDS));
    }

    private String buildPrompt(String inputJson) {
        return """
                Sen bir üniversite kulüp yönetim sistemi için çalışan yapay zeka destekli öneri asistanısın.

                Görevin:
                Verilen üye profilini, sistemde kayıtlı kulüpleri ve yaklaşan etkinlikleri analiz ederek üyeye en uygun kulüp ve etkinlik önerilerini üretmek.

                Ana hedef:
                Kullanıcıya gerçekçi, açıklanabilir ve kişiselleştirilmiş öneriler sunmak.

                Veri kaynakları:
                - memberProfile: Üyenin profil bilgileri.
                - availableClubs: Sistemde bulunan kulüpler.
                - availableEvents: Sistemde bulunan yaklaşan etkinlikler.

                Kesin kurallar:
                1. Sadece availableClubs listesinde bulunan kulüpleri öner.
                2. Sadece availableEvents listesinde bulunan etkinlikleri öner.
                3. Input içinde olmayan hiçbir kulüp, etkinlik, ID, isim veya başlık uydurma.
                4. clubId değerini sadece input içindeki gerçek clubId değerlerinden seç.
                5. eventId değerini sadece input içindeki gerçek eventId değerlerinden seç.
                6. clubName ve eventTitle değerlerini input içindeki gerçek değerlerle birebir aynı yaz.
                7. JSON alan adlarını değiştirme.
                8. Cevap sadece geçerli JSON olmalı.
                9. Markdown kullanma.
                10. JSON dışında açıklama yazma.
                11. Tüm açıklama metinlerini Türkçe yaz.
                12. matchScore değeri 0 ile 100 arasında bir tam sayı olmalı.
                13. Önerileri matchScore değerine göre yüksekten düşüğe sırala.
                14. En fazla 5 kulüp öner.
                15. En fazla 5 etkinlik öner.
                16. Uygun kulüp yoksa recommendedClubs boş liste olmalı.
                17. Uygun etkinlik yoksa recommendedEvents boş liste olmalı.
                18. availableEvents boş değilse en az 1 etkinliği değerlendirmeye çalış, fakat tamamen alakasızsa önermek zorunda değilsin.
                19. availableClubs boş değilse en az 1 kulübü değerlendirmeye çalış, fakat tamamen alakasızsa önermek zorunda değilsin.

                Analiz ederken dikkate al:
                - Üyenin bölümü
                - Fakültesi
                - Sınıf seviyesi
                - Hakkında yazısı
                - Yetenekleri
                - İlgi alanları
                - Sertifikaları
                - Bildiği diller
                - Projeleri
                - Kulüp açıklamaları
                - Etkinlik başlığı ve açıklaması
                - Etkinliğin bağlı olduğu kulüp isimleri
                - Etkinlik tarihi ve konumu

                profileSummary kuralları:
                - Türkçe yaz.
                - En fazla 3 cümle olsun.
                - Kullanıcının en güçlü ilgi alanlarını ve teknik/sosyal yönünü özetle.
                - Gereksiz uzun CV özeti gibi yazma.

                reason kuralları:
                - Türkçe yaz.
                - Kısa ama açıklayıcı olsun.
                - Önerinin neden yapıldığını somut profil bilgileriyle açıkla.
                - "Bu kulüp uygundur" gibi yüzeysel cümleler kurma.
                - Üyenin gerçek becerileri, ilgi alanları, projeleri veya bölümü ile bağlantı kur.

                matchedAreas kuralları:
                - Genel kategori isimleri yazma.
                - Şunları matchedAreas içine yazma: "Bölüm", "Yetenekler", "İlgi Alanları", "Projeler", "Hakkında Yazısı", "Sertifikalar".
                - Bunun yerine gerçek eşleşen somut kavramları yaz.
                - Örnek iyi matchedAreas:
                  ["Computer Engineering", "Spring Boot", "Backend Development", "Cloud Computing", "Docker", "Microservice Architecture"]
                - Örnek kötü matchedAreas:
                  ["Bölüm", "Yetenekler", "Projeler"]
                - Her öneri için en fazla 6 matchedAreas yaz.

                matchScore rehberi:
                - 90-100: Çok güçlü uyum. Profil ile kulüp/etkinlik doğrudan örtüşüyor.
                - 75-89: Güçlü uyum. Birkaç önemli alanda eşleşme var.
                - 60-74: Orta düzey uyum. Bazı bağlantılar var ama doğrudan değil.
                - 40-59: Zayıf uyum. Dolaylı bağlantılar var.
                - 0-39: Önermemek daha doğru olabilir.

                developmentSuggestions kuralları:
                - Türkçe yaz.
                - En fazla 4 öneri üret.
                - Öneriler kullanıcının profiline özel olsun.
                - Genel motivasyon cümleleri yazma.
                - Kulüp ve etkinliklere katılım, teknik gelişim, sosyal gelişim veya kariyer gelişimiyle bağlantı kur.

                Beklenen JSON cevap formatı:
                {
                  "profileSummary": "Türkçe kısa profil özeti",
                  "recommendedClubs": [
                    {
                      "clubId": 1,
                      "clubName": "Input içindeki gerçek kulüp adı",
                      "matchScore": 90,
                      "reason": "Bu kulübün neden önerildiğini Türkçe ve somut şekilde açıkla.",
                      "matchedAreas": ["Somut eşleşen kavram"]
                    }
                  ],
                  "recommendedEvents": [
                    {
                      "eventId": 1,
                      "eventTitle": "Input içindeki gerçek etkinlik başlığı",
                      "matchScore": 90,
                      "reason": "Bu etkinliğin neden önerildiğini Türkçe ve somut şekilde açıkla.",
                      "matchedAreas": ["Somut eşleşen kavram"]
                    }
                  ],
                  "developmentSuggestions": [
                    "Üyeye özel Türkçe gelişim önerisi"
                  ]
                }

                Input JSON:
                %s
                """.formatted(inputJson);
    }

    private String extractText(GeminiResponse response) {
        if (response == null ||
                response.getCandidates() == null ||
                response.getCandidates().isEmpty() ||
                response.getCandidates().get(0).getContent() == null ||
                response.getCandidates().get(0).getContent().getParts() == null ||
                response.getCandidates().get(0).getContent().getParts().isEmpty()) {
            throw new RuntimeException("Empty response from Gemini.");
        }

        String text = response
                .getCandidates()
                .get(0)
                .getContent()
                .getParts()
                .get(0)
                .getText();

        if (text == null || text.isBlank()) {
            throw new RuntimeException("Gemini returned blank response.");
        }

        return cleanJsonText(text);
    }

    private String cleanJsonText(String text) {
        String cleanedText = text
                .replace("```json", "")
                .replace("```", "")
                .trim();

        int firstBraceIndex = cleanedText.indexOf("{");
        int lastBraceIndex = cleanedText.lastIndexOf("}");

        if (firstBraceIndex == -1 || lastBraceIndex == -1 || firstBraceIndex > lastBraceIndex) {
            throw new RuntimeException("Gemini response does not contain valid JSON object.");
        }

        return cleanedText.substring(firstBraceIndex, lastBraceIndex + 1);
    }
}
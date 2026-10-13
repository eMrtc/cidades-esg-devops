package br.com.cidades.esg.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Component
public class GeminiClient {

    private final WebClient webClient;
    private final String apiKey;
    private final String model;

    public GeminiClient(
            @Value("${gemini.api-key:}") String apiKey,
            @Value("${gemini.base-url:https://generativelanguage.googleapis.com/v1beta}") String baseUrl,
            @Value("${gemini.model:gemini-1.5-flash}") String model) {
        this.apiKey = apiKey;
        this.model = model;
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public boolean isConfigurado() {
        return StringUtils.hasText(apiKey);
    }

    public String enviarPrompt(String prompt) {
        if (!isConfigurado()) {
            log.warn("Gemini API key não configurada. Retornando resposta padrão.");
            return "";
        }

        try {
            GeminiApiModels.Part part = GeminiApiModels.Part.builder().text(prompt).build();
            GeminiApiModels.Content content = GeminiApiModels.Content.builder().parts(List.of(part)).build();
            GeminiApiModels.GeminiRequest request = GeminiApiModels.GeminiRequest.builder()
                    .contents(List.of(content))
                    .build();

            GeminiApiModels.GeminiResponse response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/models/" + model + ":generateContent")
                            .queryParam("key", apiKey)
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(GeminiApiModels.GeminiResponse.class)
                    .block();

            return response != null ? response.getFirstText() : "";
        } catch (Exception e) {
            log.error("Erro ao chamar Gemini API: {}", e.getMessage());
            return "";
        }
    }
}

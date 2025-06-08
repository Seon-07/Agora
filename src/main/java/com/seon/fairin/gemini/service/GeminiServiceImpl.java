package com.seon.fairin.gemini.service;

import com.seon.fairin.gemini.dto.GeminiRequest;
import com.seon.fairin.config.PromptLoader;
import com.seon.fairin.gemini.dto.PromptDiv;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-06-01
 */
@Service
@RequiredArgsConstructor
public class GeminiServiceImpl implements GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.uri}")
    private String apiUrl;

    private final WebClient webClient;
    private final PromptLoader promptLoader;

    public String sendGemini(PromptDiv promptDiv, String message) {
        GeminiRequest requestBody = new GeminiRequest(promptLoader.getPrompt(promptDiv.toString()), message);
        return geminiPostRequest(requestBody);
    }

    public String geminiPostRequest(Object requestBody) {
        return webClient.post()
                .uri(apiUrl + "?key=" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}

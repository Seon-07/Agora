package com.seon.fairin.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-06-01
 */
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.uri}")
    private String apiUrl;

    private final WebClient webClient;

    public String sendString(String message) {
        System.out.println(apiUrl);
        System.out.println(apiKey);
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(Map.of(
                        "role", "user",
                        "parts", List.of(Map.of("text", message))
                ))
        );

        return webClient.post()
                .uri(apiUrl + "?key=" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}

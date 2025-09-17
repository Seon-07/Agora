package com.seon.agora.gemini.dto;

import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-06-06
 */
@Getter
public class GeminiRequest {
    private final Map<String, Object> system_instruction;
    private final List<Map<String, Object>> contents;

    public GeminiRequest(String system_instructionText, String contentText) {
        this.system_instruction = Map.of(
                "parts", List.of(
                        Map.of("text", system_instructionText)
                )
        );
        this.contents = List.of(
                Map.of("parts", List.of(
                        Map.of("text", contentText)
                ))
        );
    }
}

package com.seon.moca.gemini.dto;

import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-06-06
 */
@Getter
public class GeminiListRequest {
    private final Map<String, Object> system_instruction;
    private final List<Map<String, Object>> contents;

    public GeminiListRequest(String system_instruction, List<String> textList) {
        this.system_instruction = Map.of(
                "parts", List.of(
                        Map.of("text", system_instruction)
                )
        );
        this.contents = textList.stream()
                .map(text -> Map.of(
                        "role", "user",
                        "parts", List.of(Map.of("text", text))
                ))
                .toList();
    }
}

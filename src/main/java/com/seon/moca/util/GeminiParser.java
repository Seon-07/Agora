package com.seon.moca.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seon.common.exception.ApiException;
import com.seon.common.exception.ExceptionCode;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-06-08
 */

public class GeminiParser {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static String extractText(String jsonString) {
        JsonNode root;
        try {
            root = objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            throw new ApiException(ExceptionCode.PARSING_ERROR, "GEMINI JSON PARSING ERROR");
        }
        JsonNode textNode = root
                .path("candidates")
                .path(0)
                .path("content")
                .path("parts")
                .path(0)
                .path("text");
        if (textNode.isMissingNode()) {
            throw new RuntimeException("text 노드가 없습니다.");
        }
        return textNode.asText().trim();
    }
}
package com.seon.fairin.gemini.controller;

import com.seon.common.response.ApiResponse;
import com.seon.common.response.OperationResult;
import com.seon.fairin.gemini.service.GeminiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-06-01
 */
@Tag(name = "Gemini API", description = "Gemini API 연동")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gemini")
public class GeminiRestController {
    private final GeminiService geminiService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sentTestMessage() {
        ApiResponse responseBody = OperationResult.success(geminiService.sendGemini("test"));
        return ResponseEntity.ok().body(responseBody);
    }
}

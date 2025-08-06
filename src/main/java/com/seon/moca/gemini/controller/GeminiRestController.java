package com.seon.moca.gemini.controller;

import com.seon.common.response.ApiResponse;
import com.seon.common.response.OperationResult;
import com.seon.moca.gemini.dto.PromptDiv;
import com.seon.moca.gemini.service.GeminiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/intro")
    public ResponseEntity<ApiResponse> sentTestMessage(@RequestParam String message) {
        ApiResponse responseBody = OperationResult.success(geminiService.sendGemini(PromptDiv.intro,message));
        return ResponseEntity.ok().body(responseBody);
    }
}

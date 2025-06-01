package com.seon.fairin.chat.controller;

import com.seon.common.response.ApiResponse;
import com.seon.common.response.OperationResult;
import com.seon.fairin.chat.service.ChatService;
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
@Tag(name = "채팅", description = "채팅 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatRestController {
    private final ChatService chatService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sentTestMessage() {
        ApiResponse responseBody = OperationResult.success(chatService.sendString("안녕하세요 누구세요?"));
        return ResponseEntity.ok().body(responseBody);
    }
}

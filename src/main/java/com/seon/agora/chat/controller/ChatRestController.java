package com.seon.agora.chat.controller;

import com.seon.common.response.ApiResponse;
import com.seon.common.response.DataResult;
import com.seon.agora.chat.dto.ChatRequest;
import com.seon.agora.chat.service.ChatService;
import com.seon.agora.common.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author seon
 * @version 1.0
 * @since 25. 9. 9.
 */
@Tag(name = "채팅", description = "채팅 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRestController {
    private final ChatService chatService;


    @PostMapping
    public ResponseEntity<ApiResponse> sendChat(@RequestBody @Valid ChatRequest chatRequest, @AuthenticationPrincipal UserInfo userInfo) {
        ApiResponse responseBody = DataResult.success(chatService.sendChat(chatRequest, userInfo));
        return ResponseEntity.ok().body(responseBody);
    }
}

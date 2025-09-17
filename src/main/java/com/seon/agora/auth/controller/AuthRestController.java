package com.seon.agora.auth.controller;

import com.seon.common.response.ApiResponse;
import com.seon.common.response.OperationResult;
import com.seon.agora.auth.dto.JoinRequest;
import com.seon.agora.auth.dto.LoginRequest;
import com.seon.agora.auth.service.AuthService;
import com.seon.agora.common.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-05
 */
@Tag(name = "인증", description = "사용자 계정 인증 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthRestController {
    private final AuthService authService;
    @PostMapping("/join")
    public ResponseEntity<ApiResponse> join(@RequestBody @Valid JoinRequest request) {
        authService.join(request);
        ApiResponse responseBody = OperationResult.success("회원가입 성공");
        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid LoginRequest request, HttpServletRequest httpRequest) {
        authService.login(request, httpRequest);
        ApiResponse responseBody = OperationResult.success("로그인 성공");
        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@AuthenticationPrincipal UserInfo userInfo) {
        authService.logout(userInfo);
        ApiResponse responseBody = OperationResult.success("로그아웃 성공");
        return ResponseEntity.ok().body(responseBody);
    }
}

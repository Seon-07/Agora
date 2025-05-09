package com.seon.fairin.auth.controller;

import com.seon.common.response.ApiResponse;
import com.seon.common.response.OperationResult;
import com.seon.fairin.auth.dto.JoinRequest;
import com.seon.fairin.auth.dto.LoginRequest;
import com.seon.fairin.auth.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

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
    public ResponseEntity<ApiResponse> join(@RequestBody JoinRequest request) {
        authService.join(request);
        ApiResponse responseBody = OperationResult.success("회원가입 성공");
        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        ResponseCookie accessToken = ResponseCookie.from("ACCESS_TOKEN", token)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .sameSite("Lax")
                    .maxAge(Duration.ofMinutes(10))
                    .build();
        ApiResponse responseBody = OperationResult.success("로그인 성공");
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessToken.toString())
                .body(responseBody);
    }
}

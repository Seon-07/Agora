package com.seon.fairin.auth.controller;

import com.seon.common.response.ApiResponse;
import com.seon.common.response.OperationResult;
import com.seon.fairin.auth.dto.JoinRequest;
import com.seon.fairin.auth.dto.JwtTokens;
import com.seon.fairin.auth.dto.LoginRequest;
import com.seon.fairin.auth.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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

    private final boolean isSecure = false;

    @PostMapping("/join")
    public ResponseEntity<ApiResponse> join(@RequestBody JoinRequest request) {
        authService.join(request);
        ApiResponse responseBody = OperationResult.success("회원가입 성공");
        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        JwtTokens tokens = authService.login(request);
        ResponseCookie accessCookie = generateCookie(tokens, "ACCESS");
        ResponseCookie refreshCookie = generateCookie(tokens, "REFRESH");
        ApiResponse responseBody = OperationResult.success("로그인 성공");
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(responseBody);
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse> reissue(HttpServletRequest request) {
        JwtTokens tokens = authService.reissue(request);

        ResponseCookie accessCookie = generateCookie(tokens, "ACCESS");
        ResponseCookie refreshCookie = generateCookie(tokens, "REFRESH");

        ApiResponse responseBody = OperationResult.success("토큰 재발급");
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(responseBody);
    }

    public ResponseCookie generateCookie(JwtTokens tokens, String type) {
        String tokenType = type.equals("ACCESS") ? "ACCESS_TOKEN" : "REFRESH_TOKEN";
        String token = type.equals("ACCESS") ? tokens.getAccessToken() : tokens.getRefreshToken();
        Duration maxAge = type.equals("ACCESS") ? Duration.ofMinutes(5) : Duration.ofDays(7);

        return ResponseCookie.from(tokenType, token)
                .httpOnly(true)
                .secure(isSecure)
                .path("/")
                .sameSite("Lax")
                .maxAge(maxAge)
                .build();
    }
}

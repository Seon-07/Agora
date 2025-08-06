package com.seon.moca.auth.controller;

import com.seon.common.response.ApiResponse;
import com.seon.common.response.OperationResult;
import com.seon.moca.auth.dto.JoinRequest;
import com.seon.moca.auth.dto.JwtToken;
import com.seon.moca.auth.dto.JwtTokens;
import com.seon.moca.auth.dto.LoginRequest;
import com.seon.moca.auth.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    public ResponseEntity<ApiResponse> join(@RequestBody @Valid JoinRequest request) {
        authService.join(request);
        ApiResponse responseBody = OperationResult.success("회원가입 성공");
        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid LoginRequest request) {
        JwtTokens tokens = authService.login(request);
        ResponseCookie accessCookie = generateCookie(tokens, JwtToken.ACCESS_TOKEN.getType());
        ResponseCookie refreshCookie = generateCookie(tokens, JwtToken.REFRESH_TOKEN.getType());
        ApiResponse responseBody = OperationResult.success("로그인 성공");
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(responseBody);
    }
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletRequest request) {
        //엑세스 토큰 삭제(클라이언트)
        ResponseCookie deleteAccessCookie = ResponseCookie.from(JwtToken.ACCESS_TOKEN.name(), "")
                .httpOnly(true)
                .secure(isSecure)
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();
        //리프레시 토큰 삭제(클라이언트)
        ResponseCookie deleteRefreshCookie = ResponseCookie.from(JwtToken.REFRESH_TOKEN.name(), "")
                .httpOnly(true)
                .secure(isSecure)
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();

        //Redis 에서 토큰 제거
        authService.logout(request);
        ApiResponse responseBody = OperationResult.success("로그아웃 성공");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteAccessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, deleteRefreshCookie.toString())
                .body(responseBody);
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse> reissue(HttpServletRequest request) {
        JwtTokens tokens = authService.reissue(request);

        ResponseCookie accessCookie = generateCookie(tokens, JwtToken.ACCESS_TOKEN.getType());
        ResponseCookie refreshCookie = generateCookie(tokens, JwtToken.REFRESH_TOKEN.getType());

        ApiResponse responseBody = OperationResult.success("토큰 재발급");
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(responseBody);
    }

    public ResponseCookie generateCookie(JwtTokens tokens, String type) {
        String tokenType = type.equals(JwtToken.ACCESS_TOKEN.getType()) ? JwtToken.ACCESS_TOKEN.name() : JwtToken.REFRESH_TOKEN.name();
        String token = type.equals(JwtToken.ACCESS_TOKEN.getType()) ? tokens.getAccessToken() : tokens.getRefreshToken();
        Duration maxAge = type.equals(JwtToken.ACCESS_TOKEN.getType()) ? Duration.ofMinutes(5) : Duration.ofDays(7);

        return ResponseCookie.from(tokenType, token)
                .httpOnly(true)
                .secure(isSecure)
                .path("/")
                .sameSite("Lax")
                .maxAge(maxAge)
                .build();
    }
}

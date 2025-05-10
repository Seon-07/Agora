package com.seon.fairin.auth.service;

import com.seon.common.exception.ApiException;
import com.seon.common.exception.ExceptionCode;
import com.seon.common.util.IdGenerater;
import com.seon.fairin.auth.dto.JoinRequest;
import com.seon.fairin.auth.dto.LoginRequest;
import com.seon.fairin.auth.entity.User;
import com.seon.fairin.auth.repository.AuthRepository;
import com.seon.fairin.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-06
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    @Override
    public void join(JoinRequest joinRequest) {
        List<User> duplicates = authRepository.findDuplicates(joinRequest.getUserId(), joinRequest.getEmail(), joinRequest.getNickname());
        for (User user : duplicates) {
            if (user.getUserId().equals(joinRequest.getUserId())) throw new ApiException(ExceptionCode.BAD_REQUEST, "ID가 중복");
            if (user.getEmail().equals(joinRequest.getEmail())) throw new ApiException(ExceptionCode.BAD_REQUEST, "이메일 중복");
            if (user.getNickname().equals(joinRequest.getNickname())) throw new ApiException(ExceptionCode.BAD_REQUEST, "닉네임 중복");
        }
        User user = User.builder()
                .id(IdGenerater.generate())
                .userId(joinRequest.getUserId())
                .pw(passwordEncoder.encode(joinRequest.getPw()))
                .email(joinRequest.getEmail())
                .name(joinRequest.getName())
                .nickname(joinRequest.getNickname())
                .build();
        authRepository.save(user);
    }

    @Override
    public String login(LoginRequest loginRequest) {
        User user = authRepository.findByUserId(loginRequest.getUserId())
                .orElseThrow(() -> new ApiException(ExceptionCode.INVALID_CREDENTIALS));
        String inputPw = loginRequest.getPw();
        if (!passwordEncoder.matches(inputPw, user.getPw())) {
            throw new ApiException(ExceptionCode.INVALID_CREDENTIALS);
        }
        log.info(user.getUserId() + "로그인");
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());
        redisTemplate.opsForValue().set("RFT:" + user.getUserId(), refreshToken, 7, TimeUnit.DAYS);
        log.info("RFT:" + user.getUserId() + " = " + refreshToken + "생성");
        return jwtTokenProvider.createAccessToken(user.getUserId());
    }

    @Override
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = getToken(request);
        String userId = jwtTokenProvider.getUserIdIgnoreExpiration(accessToken);
        String savedRefreshToken = redisTemplate.opsForValue().get("RFT:" + userId);
        if (savedRefreshToken == null || !jwtTokenProvider.validateToken(savedRefreshToken)) {
            throw new ApiException(ExceptionCode.UNAUTHORIZED);
        }
        String newAccessToken = jwtTokenProvider.createAccessToken(userId);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(userId);
        redisTemplate.opsForValue().set("RFT:" + userId, newRefreshToken, 7, TimeUnit.DAYS);

        ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", newAccessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ofMinutes(5))
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
    }

    public String getToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("ACCESS_TOKEN")) {
                return cookie.getValue();
            }
        }
        return null;
    }
}

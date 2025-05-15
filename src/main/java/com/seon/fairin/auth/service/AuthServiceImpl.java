package com.seon.fairin.auth.service;

import com.seon.common.exception.ApiException;
import com.seon.common.exception.ExceptionCode;
import com.seon.common.util.IdGenerater;
import com.seon.fairin.auth.dto.JoinRequest;
import com.seon.fairin.auth.dto.JwtTokens;
import com.seon.fairin.auth.dto.LoginRequest;
import com.seon.fairin.auth.entity.User;
import com.seon.fairin.auth.repository.AuthRepository;
import com.seon.fairin.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public JwtTokens login(LoginRequest loginRequest) {
        User user = authRepository.findByUserId(loginRequest.getUserId())
                .orElseThrow(() -> new ApiException(ExceptionCode.INVALID_CREDENTIALS));
        String inputPw = loginRequest.getPw();
        if (!passwordEncoder.matches(inputPw, user.getPw())) {
            throw new ApiException(ExceptionCode.INVALID_CREDENTIALS);
        }
        log.info(user.getUserId() + "로그인");

        String refreshToken = jwtTokenProvider.createRefreshToken(user);
        String accessToken = jwtTokenProvider.createAccessToken(user);

        redisTemplate.opsForValue().set("RFT:" + user.getUserId(), refreshToken, 7, TimeUnit.DAYS);
        log.info("RFT:" + user.getUserId() + " = " + refreshToken + "생성");

        return JwtTokens.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public JwtTokens reissue(HttpServletRequest request) {
        //리프레시 토큰에서 userId 가져오기
        String refreshToken = getToken(request);
        String userId = jwtTokenProvider.getUserId(refreshToken);
        //유저 찾아오기
        User user = authRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(ExceptionCode.NOT_FOUND));
        //저장된 refreshToken 가져오기
        String savedRefreshToken = redisTemplate.opsForValue().get("RFT:" + userId);
        if (savedRefreshToken == null || !jwtTokenProvider.validateToken(savedRefreshToken)) {
            throw new ApiException(ExceptionCode.UNAUTHORIZED);
        }
        //새로운 accessToken, refreshToken 생성
        String newAccessToken = jwtTokenProvider.createAccessToken(user);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user);
        //새로운 refreshToken Redis에 저장
        redisTemplate.opsForValue().set("RFT:" + userId, newRefreshToken, 7, TimeUnit.DAYS);

        return JwtTokens.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public String getToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("REFRESH_TOKEN")) {
                return cookie.getValue();
            }
        }
        return null;
    }
}


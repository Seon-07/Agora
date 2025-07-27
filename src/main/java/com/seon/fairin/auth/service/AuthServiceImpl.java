package com.seon.fairin.auth.service;

import com.seon.common.exception.ApiException;
import com.seon.common.exception.ExceptionCode;
import com.seon.common.util.IdGenerater;
import com.seon.fairin.auth.dto.JoinRequest;
import com.seon.fairin.auth.dto.JwtTokens;
import com.seon.fairin.auth.dto.LoginRequest;
import com.seon.fairin.common.service.RedisService;
import com.seon.fairin.user.entity.User;
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
    private final RedisService redisService;


    /**
     * 회원가입
     */
    @Transactional
    @Override
    public void join(JoinRequest joinRequest) {
        List<User> duplicates = authRepository.findDuplicates(joinRequest.getUserId(), joinRequest.getEmail(), joinRequest.getNickname());
        for (User user : duplicates) {
            //중복 아이디 확인
            if (user.getUserId().equals(joinRequest.getUserId())) throw new ApiException(ExceptionCode.BAD_REQUEST, "이미 사용중인 아이디입니다.");
            //중복 이메일 확인
            if (user.getEmail().equals(joinRequest.getEmail())) throw new ApiException(ExceptionCode.BAD_REQUEST, "이미 사용중인 이메일입니다.");
            //중복 닉네임 확인
            if (user.getNickname().equals(joinRequest.getNickname())) throw new ApiException(ExceptionCode.BAD_REQUEST, "이미 사용중인 닉네임입니다.");
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

    /**
     * 로그인
     */
    @Override
    public JwtTokens login(LoginRequest loginRequest) {
        //아이디 찾기
        User user = authRepository.findByUserId(loginRequest.getUserId())
                .orElseThrow(() -> new ApiException(ExceptionCode.INVALID_CREDENTIALS));
        String inputPw = loginRequest.getPw();
        //검색된 아이디 비밀번호와 입력 비밀번호 비교
        if (!passwordEncoder.matches(inputPw, user.getPw())) {
            throw new ApiException(ExceptionCode.INVALID_CREDENTIALS);
        }
        //사용불가능 계정 판단
        if (!user.isUseYn()) throw new ApiException(ExceptionCode.FORBIDDEN, "사용할 수 없는 계정입니다.");
        if (user.isDelYn()) throw new ApiException(ExceptionCode.FORBIDDEN, "삭제된 계정입니다.");

        log.info(user.getUserId() + "로그인");

        //로그인 성공 시 토큰 생성
        String refreshToken = jwtTokenProvider.createRefreshToken(user);
        String accessToken = jwtTokenProvider.createAccessToken(user);

        //레디스에 리프레시 토큰 저장
        redisService.set("RFT:" + user.getUserId(), refreshToken, 7, TimeUnit.DAYS);

        return JwtTokens.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 로그아웃
     */
    @Override
    public void logout(HttpServletRequest request) {
        //토큰 가져오기
        String refreshToken = getToken(request);

        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            log.warn("유효하지 않거나 존재하지 않는 리프레시 토큰.");
            return;
        }
        String userId = jwtTokenProvider.getUserId(refreshToken);
        String key = "RFT:" + userId;

        boolean existed = redisService.delete(key);
        if (existed) {
            log.info("Redis 리프레시 토큰 삭제: {}", key);
        } else {
            log.warn("Redis에 존재하지 않는 키: {}", key);
        }
    }

    /**
     * 토큰 재발급
     * - 현재 클라이언트 refresh 토큰이 유효하면 새로운 access, refresh 토큰 재발급
     */
    @Override
    public JwtTokens reissue(HttpServletRequest request) {
        //리프레시 토큰에서 userId 가져오기
        String refreshToken = getToken(request);
        String userId = jwtTokenProvider.getUserId(refreshToken);
        //유저 찾아오기
        User user = authRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(ExceptionCode.NOT_FOUND));
        //저장된 refreshToken 가져오기
        String savedRefreshToken = redisService.get("RFT:" + userId);
        if (savedRefreshToken == null || !jwtTokenProvider.validateToken(savedRefreshToken)) {
            throw new ApiException(ExceptionCode.UNAUTHORIZED);
        }
        //새로운 accessToken, refreshToken 생성
        String newAccessToken = jwtTokenProvider.createAccessToken(user);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user);
        //새로운 refreshToken Redis에 저장
        redisService.set("RFT:" + userId, newRefreshToken, 7, TimeUnit.DAYS);

        return JwtTokens.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    /**
     * 현재 접속중인 클라이언트의 refresh 토큰 가져오기
     */
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


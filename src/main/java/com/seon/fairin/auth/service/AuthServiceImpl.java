package com.seon.fairin.auth.service;

import com.seon.common.exception.ApiException;
import com.seon.common.exception.ExceptionCode;
import com.seon.common.util.IdGenerater;
import com.seon.fairin.auth.dto.JoinRequest;
import com.seon.fairin.auth.dto.LoginRequest;
import com.seon.fairin.auth.entity.User;
import com.seon.fairin.auth.repository.AuthRepository;
import com.seon.fairin.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-06
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

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
                .orElseThrow(() -> new ApiException(ExceptionCode.BAD_REQUEST, "아이디 또는 비밀번호 오류"));
        String inputPw = loginRequest.getPw();
        if (!passwordEncoder.matches(inputPw, user.getPw())) {
            System.out.println(inputPw);
            System.out.println(user.getPw());
            throw new ApiException(ExceptionCode.BAD_REQUEST, "아이디 또는 비밀번호 오류");
        }
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());
        //레디스에 리프레시토큰 저장
        return jwtTokenProvider.createAccessToken(user.getUserId());
    }


}

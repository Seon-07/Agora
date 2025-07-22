package com.seon.fairin.auth.service;

import com.seon.common.exception.ApiException;
import com.seon.common.exception.ExceptionCode;
import com.seon.fairin.auth.dto.JoinRequest;
import com.seon.fairin.auth.dto.LoginRequest;
import com.seon.fairin.auth.repository.AuthRepository;
import com.seon.fairin.jwt.JwtTokenProvider;
import com.seon.fairin.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-07-20
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private AuthRepository authRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    @DisplayName("회원가입_아이디 중복 테스트_(TC_JOIN_001)")
    void join_duplicate_userId() {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setUserId("testId");
        joinRequest.setPw("testPw1234!");
        joinRequest.setEmail("testEmail@test.com");
        joinRequest.setName("테스트");
        joinRequest.setNickname("testNickname");

        User user = User.builder().userId("testId").email("otherEmail@test.com").nickname("otherNickname").build();

        assertEquals(joinRequest.getUserId(), user.getUserId(), "아이디 같음");
        assertNotEquals(joinRequest.getEmail(), user.getEmail(), "이메일 다름");
        assertNotEquals(joinRequest.getNickname(), user.getNickname(), "닉네임 다름");

        given(authRepository.findDuplicates(any(), any(), any())).willReturn(List.of(user));

        ApiException ex = assertThrows(ApiException.class, () -> authService.join(joinRequest));
        assertEquals("이미 사용중인 아이디입니다.", ex.getMessage());
    }

    @Test
    @DisplayName("회원가입_이메일 중복 테스트_(TC_JOIN_002)")
    void join_duplicate_email() {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setUserId("testId");
        joinRequest.setPw("testPw1234!");
        joinRequest.setEmail("testEmail@test.com");
        joinRequest.setName("테스트");
        joinRequest.setNickname("testNickname");

        User user = User.builder().userId("otherId").email("testEmail@test.com").nickname("otherNickname").build();

        given(authRepository.findDuplicates(any(), any(), any())).willReturn(List.of(user));

        ApiException ex = assertThrows(ApiException.class, () -> authService.join(joinRequest));
        assertEquals("이미 사용중인 이메일입니다.", ex.getMessage());
    }

    @Test
    @DisplayName("회원가입_닉네임 중복 테스트_(TC_JOIN_003)")
    void join_duplicate_nickname() {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setUserId("testId");
        joinRequest.setPw("testPw1234!");
        joinRequest.setEmail("testEmail@test.com");
        joinRequest.setName("테스트");
        joinRequest.setNickname("testNickname");
        User user = User.builder().userId("otherId").email("otherEmail@test.com").nickname("testNickname").build();

        given(authRepository.findDuplicates(any(), any(), any())).willReturn(List.of(user));

        ApiException ex = assertThrows(ApiException.class, () -> authService.join(joinRequest));
        assertEquals("이미 사용중인 닉네임입니다.", ex.getMessage());
    }

    @Test
    @DisplayName("회원가입_다중 중복 테스트_(TC_JOIN_004)")
    void join_duplicate_multiple() {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setUserId("testId");
        joinRequest.setPw("testPw1234!");
        joinRequest.setEmail("testEmail@test.com");
        joinRequest.setName("테스트");
        joinRequest.setNickname("testNickname");
        User user = User.builder().userId("testId").email("testEmail@test.com").nickname("testNickname").build();

        given(authRepository.findDuplicates(any(), any(), any())).willReturn(List.of(user));

        ApiException ex = assertThrows(ApiException.class, () -> authService.join(joinRequest));
        assertEquals("이미 사용중인 아이디입니다.", ex.getMessage());
    }


    @Test
    @DisplayName("회원가입_성공_(TC_JOIN_005)")
    void join_success() {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setUserId("testId");
        joinRequest.setPw("testPw1234!");
        joinRequest.setEmail("testEmail@test.com");
        joinRequest.setName("테스트");
        joinRequest.setNickname("testNickname");

        assertDoesNotThrow(() -> authService.join(joinRequest));
        verify(authRepository).save(any(User.class));
    }

    @Test
    @DisplayName("로그인_아이디 없음 테스트_(TC_LOGIN_001)")
    void login_noId() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserId("testId");
        loginRequest.setPw("testPw1234!");

        given(authRepository.findByUserId(loginRequest.getUserId())).willReturn(Optional.empty());

        ApiException ex = assertThrows(ApiException.class, () -> authService.login(loginRequest));
        assertEquals(ExceptionCode.INVALID_CREDENTIALS, ex.getExceptionCode());
    }

    @Test
    @DisplayName("로그인_잘못된 비밀번호 테스트_(TC_LOGIN_002)")
    void login_wrongPw() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserId("testId");
        loginRequest.setPw("testPw1234!");

        User user = User.builder().userId("testId").pw("hashedPw").build();
        given(authRepository.findByUserId(loginRequest.getUserId())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(loginRequest.getPw(), "hashedPw")).willReturn(false);

        ApiException ex = assertThrows(ApiException.class, () -> authService.login(loginRequest));
        assertEquals(ExceptionCode.INVALID_CREDENTIALS, ex.getExceptionCode());
    }


    @Test
    @DisplayName("로그인_사용불가 아이디 테스트_(TC_LOGIN_003)")
    void login_notUse() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserId("testId");
        loginRequest.setPw("testPw1234!");

        User user = User.builder().userId("testId").pw("hashedPw").useYn(false).delYn(false).build();
        given(authRepository.findByUserId(loginRequest.getUserId())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(loginRequest.getPw(), "hashedPw")).willReturn(true);

        ApiException ex = assertThrows(ApiException.class, () -> authService.login(loginRequest));
        assertEquals(ExceptionCode.FORBIDDEN, ex.getExceptionCode());
    }

    //@Test
    void logout() {
    }

    //@Test
    void reissue() {
    }

    //@Test
    void getToken() {
    }
}
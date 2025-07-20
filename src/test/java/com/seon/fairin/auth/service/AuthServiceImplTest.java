package com.seon.fairin.auth.service;

import com.seon.common.exception.ApiException;
import com.seon.fairin.auth.dto.JoinRequest;
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
    @DisplayName("회원가입_아이디 중복 테스트")
    void join_duplicate_userId() {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setUserId("testId");
        joinRequest.setPw("testPw1234!");
        joinRequest.setEmail("testEmail@test.com");
        joinRequest.setName("테스트");
        joinRequest.setNickname("testNickname");

        User user = User.builder().userId("testId").email("other@test.com").nickname("테스트2").build();

        assertEquals(joinRequest.getUserId(), user.getUserId(), "아이디 같음");
        assertNotEquals(joinRequest.getEmail(), user.getEmail(), "이메일 다름");
        assertNotEquals(joinRequest.getNickname(), user.getNickname(), "닉네임 다름");

        given(authRepository.findDuplicates(any(), any(), any())).willReturn(List.of(user));

        ApiException ex = assertThrows(ApiException.class, () -> authService.join(joinRequest));
        assertEquals("이미 사용중인 아이디입니다.", ex.getMessage());
    }

    @Test
    @DisplayName("회원가입_이메일 중복 테스트")
    void join_duplicate_email() {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setUserId("testId");
        joinRequest.setPw("testPw1234!");
        joinRequest.setEmail("testEmail@test.com");
        joinRequest.setName("테스트");
        joinRequest.setNickname("testNickname");

        User user = User.builder().userId("testId2").email("testEmail@test.com").nickname("otherNick").build();

        assertNotEquals(joinRequest.getUserId(), user.getUserId(), "아이디 다름");
        assertEquals(joinRequest.getEmail(), user.getEmail(), "이메일 같음");
        assertNotEquals(joinRequest.getNickname(), user.getNickname(), "닉네임 다름");

        given(authRepository.findDuplicates(any(), any(), any())).willReturn(List.of(user));

        ApiException ex = assertThrows(ApiException.class, () -> authService.join(joinRequest));
        assertEquals("이미 사용중인 이메일입니다.", ex.getMessage());
    }

    @Test
    @DisplayName("회원가입_닉네임 중복 테스트")
    void join_duplicate_nickname() {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setUserId("testId");
        joinRequest.setPw("testPw1234!");
        joinRequest.setEmail("testEmail@test.com");
        joinRequest.setName("테스트");
        joinRequest.setNickname("testNickname");
        User user = User.builder().userId("testId2").email("other@test.com").nickname("testNickname").build();

        assertNotEquals(joinRequest.getUserId(), user.getUserId(), "아이디 다름");
        assertNotEquals(joinRequest.getEmail(), user.getEmail(), "이메일 다름");
        assertEquals(joinRequest.getNickname(), user.getNickname(), "닉네임 같음");

        given(authRepository.findDuplicates(any(), any(), any())).willReturn(List.of(user));

        ApiException ex = assertThrows(ApiException.class, () -> authService.join(joinRequest));
        assertEquals("이미 사용중인 닉네임입니다.", ex.getMessage());
    }

    @Test
    @DisplayName("회원가입_다중 중복 테스트")
    void join_duplicate_multiple() {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setUserId("testId");
        joinRequest.setPw("testPw1234!");
        joinRequest.setEmail("testEmail@test.com");
        joinRequest.setName("테스트");
        joinRequest.setNickname("testNickname");
        User user = User.builder().userId("testId").email("testEmail@test.com").nickname("testNickname").build();

        assertEquals(joinRequest.getUserId(), user.getUserId(), "아이디 같음");
        assertEquals(joinRequest.getEmail(), user.getEmail(), "이메일 같음");
        assertEquals(joinRequest.getNickname(), user.getNickname(), "닉네임 같음");

        given(authRepository.findDuplicates(any(), any(), any())).willReturn(List.of(user));

        ApiException ex = assertThrows(ApiException.class, () -> authService.join(joinRequest));
        assertEquals("이미 사용중인 아이디입니다.", ex.getMessage());
    }


    @Test
    @DisplayName("회원가입_성공")
    void join_success() {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setUserId("testId");
        joinRequest.setPw("testPw1234!");
        joinRequest.setEmail("testEmail@test.com");
        joinRequest.setName("테스트");
        joinRequest.setNickname("testNickname");

        given(authRepository.findDuplicates(any(), any(), any())).willReturn(List.of());

        assertDoesNotThrow(() -> authService.join(joinRequest));
        verify(authRepository).save(any(User.class));
    }

    //@Test
    void login() {
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
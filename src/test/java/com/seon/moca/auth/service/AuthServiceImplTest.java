package com.seon.moca.auth.service;

import com.seon.common.exception.ApiException;
import com.seon.common.exception.ExceptionCode;
import com.seon.moca.auth.dto.JoinRequest;
import com.seon.moca.auth.dto.LoginRequest;
import com.seon.moca.auth.repository.AuthRepository;
import com.seon.moca.user.entity.User;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Nested
    @DisplayName("회원가입 테스트")
    class joinTests {
        @Test
        @DisplayName("회원가입_아이디 중복 테스트_(TC_GUI_001)")
        void join_duplicate_userId() {
            JoinRequest joinRequest = new JoinRequest();
            joinRequest.setUserId("testId");
            joinRequest.setPw("testPw1234!");
            joinRequest.setEmail("testEmail@test.com");
            joinRequest.setName("테스트");
            joinRequest.setNickname("testNickname");
            User user = User.builder()
                    .userId("testId")
                    .email("otherEmail@test.com")
                    .nickname("otherNickname")
                    .build();

            given(authRepository.findDuplicates(any(), any(), any())).willReturn(List.of(user));

            ApiException ex = assertThrows(ApiException.class, () -> authService.join(joinRequest));
            assertEquals("이미 사용중인 아이디입니다.", ex.getMessage());
        }

        @Test
        @DisplayName("회원가입_성공_(TC_GUI_005)")
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
    }

    @Nested
    @DisplayName("로그인 테스트")
    class loginTests {
        @Test
        @DisplayName("로그인_아이디 없음 테스트_(TC_LGI_001)")
        void login_noId() {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUserId("testId");
            loginRequest.setPw("testPw1234!");

            given(authRepository.findByUserId(loginRequest.getUserId())).willReturn(Optional.empty());

            ApiException ex = assertThrows(ApiException.class, () -> authService.login(loginRequest, new MockHttpServletRequest()));
            assertEquals(ExceptionCode.INVALID_CREDENTIALS, ex.getExceptionCode());
        }

        @Test
        @DisplayName("로그인_성공_(TC_LGI_005)")
        void login_success() {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUserId("testId");
            loginRequest.setPw("testPw1234!");

            User user = User.builder()
                    .userId("testId")
                    .pw("hashedPw")
                    .useYn(true)
                    .delYn(false)
                    .build();

            given(authRepository.findByUserId(loginRequest.getUserId())).willReturn(Optional.of(user));
            given(passwordEncoder.matches(loginRequest.getPw(), "hashedPw")).willReturn(true);

            MockHttpServletRequest request = new MockHttpServletRequest();
            boolean result = authService.login(loginRequest, request);

            assertTrue(result);
            HttpSession session = request.getSession(false);
            assertNotNull(session);
            assertEquals("testId", session.getAttribute("LOGIN_USER"));
        }
    }

    @Nested
    @DisplayName("로그아웃 테스트")
    class logoutTests {
        @Test
        @DisplayName("로그아웃_성공_(TC_LGO_001)")
        void logout_success() {

        }
    }
}
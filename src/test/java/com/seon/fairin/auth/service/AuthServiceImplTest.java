package com.seon.fairin.auth.service;

import com.seon.common.exception.ApiException;
import com.seon.common.exception.ExceptionCode;
import com.seon.fairin.auth.dto.JoinRequest;
import com.seon.fairin.auth.dto.JwtTokens;
import com.seon.fairin.auth.dto.LoginRequest;
import com.seon.fairin.auth.repository.AuthRepository;
import com.seon.fairin.common.service.RedisService;
import com.seon.fairin.jwt.JwtTokenProvider;
import com.seon.fairin.user.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
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
    private HttpServletRequest request;

    @Mock
    private RedisService redisService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    @Nested
    @DisplayName("회원가입 테스트")
    class joinTests{
        @Test
        @DisplayName("회원가입_아이디 중복 테스트_(TC_GUI_001)")
        void join_duplicate_userId() {
            JoinRequest joinRequest = new JoinRequest();
            joinRequest.setUserId("testId");
            joinRequest.setPw("testPw1234!");
            joinRequest.setEmail("testEmail@test.com");
            joinRequest.setName("테스트");
            joinRequest.setNickname("testNickname");
            User user = User.builder().userId("testId").email("otherEmail@test.com").nickname("otherNickname").build();

            given(authRepository.findDuplicates(any(), any(), any())).willReturn(List.of(user));

            ApiException ex = assertThrows(ApiException.class, () -> authService.join(joinRequest));
            assertEquals("이미 사용중인 아이디입니다.", ex.getMessage());
        }

        @Test
        @DisplayName("회원가입_이메일 중복 테스트_(TC_GUI_002)")
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
        @DisplayName("회원가입_닉네임 중복 테스트_(TC_GUI_003)")
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
        @DisplayName("회원가입_다중 중복 테스트_(TC_GUI_004)")
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

            ApiException ex = assertThrows(ApiException.class, () -> authService.login(loginRequest));
            assertEquals(ExceptionCode.INVALID_CREDENTIALS, ex.getExceptionCode());
        }

        @Test
        @DisplayName("로그인_잘못된 비밀번호 테스트_(TC_LGI_002)")
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
        @DisplayName("로그인_사용불가 아이디 테스트_(TC_LGI_003)")
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

        @Test
        @DisplayName("로그인_삭제된 아이디 테스트_(TC_LGI_004)")
        void login_deleteUser() {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUserId("testId");
            loginRequest.setPw("testPw1234!");
            User user = User.builder().userId("testId").pw("hashedPw").useYn(true).delYn(true).build();

            given(authRepository.findByUserId(loginRequest.getUserId())).willReturn(Optional.of(user));
            given(passwordEncoder.matches(loginRequest.getPw(), "hashedPw")).willReturn(true);

            ApiException ex = assertThrows(ApiException.class, () -> authService.login(loginRequest));
            assertEquals(ExceptionCode.FORBIDDEN, ex.getExceptionCode());
        }

        @Test
        @DisplayName("로그인_성공_(TC_LGI_005)")
        void login_success() {
            String newAccessToken = "newAccessToken";
            String newRefreshToken = "newRefreshToken";
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUserId("testId");
            loginRequest.setPw("testPw1234!");
            User user = User.builder().userId("testId").pw("hashedPw").useYn(true).delYn(false).build();

            given(authRepository.findByUserId(loginRequest.getUserId())).willReturn(Optional.of(user));
            given(passwordEncoder.matches(loginRequest.getPw(), "hashedPw")).willReturn(true);
            given(jwtTokenProvider.createAccessToken(user)).willReturn(newAccessToken);
            given(jwtTokenProvider.createRefreshToken(user)).willReturn(newRefreshToken);

            JwtTokens tokens = authService.login(loginRequest);

            assertEquals(newAccessToken, tokens.getAccessToken());
            assertEquals(newRefreshToken, tokens.getRefreshToken());
            verify(redisService).set("RFT:" + user.getUserId(), newRefreshToken, 7, TimeUnit.DAYS);
        }
    }

    @Nested
    @DisplayName("로그아웃 테스트")
    class logoutTests {
        @Test
        @DisplayName("로그아웃_토큰 없음 테스트_(TC_LGO_001)")
        void logout_noToken() {
            given(request.getCookies()).willReturn(null);

            authService.logout(request);

            verify(jwtTokenProvider, never()).validateToken(any());
            verify(redisService, never()).delete(anyString());
        }

        @Test
        @DisplayName("로그아웃_토큰 키 없음 테스트_(TC_LGO_002)")
        void logout_noTokenKey() {
            String token = "token";
            String userId = "testId";
            String key = "RFT:" + userId;
            Cookie refreshCookie = new Cookie("REFRESH_TOKEN", token);

            given(request.getCookies()).willReturn(new Cookie[] {refreshCookie});
            given(jwtTokenProvider.validateToken(token)).willReturn(true);
            given(jwtTokenProvider.getUserId(token)).willReturn(userId);
            given(redisService.delete(key)).willReturn(false);

            authService.logout(request);

            verify(redisService).delete(key);
        }

        @Test
        @DisplayName("로그아웃_성공_(TC_LGO_003)")
        void logout_success() {
            String token = "token";
            String userId = "testId";
            String key = "RFT:" + userId;
            Cookie refreshCookie = new Cookie("REFRESH_TOKEN", token);

            given(request.getCookies()).willReturn(new Cookie[] {refreshCookie});
            given(jwtTokenProvider.validateToken(token)).willReturn(true);
            given(jwtTokenProvider.getUserId(token)).willReturn(userId);
            given(redisService.delete(key)).willReturn(true);

            authService.logout(request);

            verify(redisService).delete(key);
        }
    }

    @Nested
    @DisplayName("토큰 재발급 테스트")
    class reissueTests {
        @Test
        @DisplayName("토큰 재발급_사용자 없음 테스트_(TC_TRG_001)")
        void reissue_noUser() {
            String token = "token";
            String userId = "testId";
            Cookie refreshCookie = new Cookie("REFRESH_TOKEN", token);

            given(request.getCookies()).willReturn(new Cookie[] { refreshCookie });
            given(jwtTokenProvider.getUserId(token)).willReturn(userId);
            given(authRepository.findByUserId(userId)).willReturn(Optional.empty());

            ApiException ex = assertThrows(ApiException.class, () -> authService.reissue(request));
            assertEquals(ExceptionCode.NOT_FOUND, ex.getExceptionCode());
        }

        @Test
        @DisplayName("토큰 재발급_리프레시 토큰 없음 테스트_(TC_TRG_002)")
        void reissue_noToken() {
            String token = "token";
            String userId = "testId";
            User user = User.builder().userId(userId).build();
            Cookie refreshCookie = new Cookie("REFRESH_TOKEN", token);

            given(request.getCookies()).willReturn(new Cookie[] { refreshCookie });
            given(jwtTokenProvider.getUserId(token)).willReturn(userId);
            given(authRepository.findByUserId(userId)).willReturn(Optional.of(user));

            given(redisService.get("RFT:" + userId)).willReturn(null);

            ApiException ex = assertThrows(ApiException.class, () -> authService.reissue(request));
            assertEquals(ExceptionCode.UNAUTHORIZED, ex.getExceptionCode());
        }

        @Test
        @DisplayName("토큰 재발급_성공_(TC_TRG_003)")
        void reissue_success() {
            String token = "token";
            String userId = "testId";
            String savedToken = "refresh-token";
            String newAccessToken = "newAccessToken";
            String newRefreshToken = "newRefreshToken";
            User user = User.builder().userId(userId).build();
            Cookie refreshCookie = new Cookie("REFRESH_TOKEN", token);

            given(request.getCookies()).willReturn(new Cookie[] { refreshCookie });
            given(jwtTokenProvider.getUserId(token)).willReturn(userId);
            given(authRepository.findByUserId(userId)).willReturn(Optional.of(user));
            given(redisService.get("RFT:" + userId)).willReturn(savedToken);
            given(jwtTokenProvider.validateToken(savedToken)).willReturn(true);
            given(jwtTokenProvider.createAccessToken(user)).willReturn(newAccessToken);
            given(jwtTokenProvider.createRefreshToken(user)).willReturn(newRefreshToken);

            JwtTokens tokens = authService.reissue(request);

            assertEquals(newAccessToken, tokens.getAccessToken());
            assertEquals(newRefreshToken, tokens.getRefreshToken());
            verify(redisService).set("RFT:" + userId, newRefreshToken, 7, TimeUnit.DAYS);
        }
    }

    @Nested
    @DisplayName("토큰 추출 테스트")
    class getTokenTests {
        @Test
        @DisplayName("토큰 추출_쿠키 없음_(TC_GTK_001)")
        void getToken_noCookies() {
            given(request.getCookies()).willReturn(null);

            String token = authService.getToken(request);

            assertNull(token);
        }

        @Test
        @DisplayName("토큰 추출_리프레시 쿠키 없음_(TC_GTK_002)")
        void getToken_noRefreshToken() {
            Cookie[] cookies = new Cookie[] {
                    new Cookie("SESSION", "testSession"),
                    new Cookie("OTHER", "other")
            };
            given(request.getCookies()).willReturn(cookies);

            String token = authService.getToken(request);

            assertNull(token);
        }

        @Test
        @DisplayName("토큰 추출_성공_(TC_GTK_003)")
        void getToken_success() {
            String refreshToken = "refresh-token";
            Cookie[] cookies = new Cookie[] {
                    new Cookie("SESSION", "testSession"),
                    new Cookie("REFRESH_TOKEN", refreshToken)
            };
            given(request.getCookies()).willReturn(cookies);

            String token = authService.getToken(request);

            assertEquals(refreshToken, token);
        }
    }
    }



package com.seon.fairin.jwt;

import com.seon.common.exception.ExceptionCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-06
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        log.info("URI: {}", uri); //사용자의 요청
        // auth 관련 요청은 다음 필터로
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) ||
                uri.equals("/api/auth/login") || //로그인
                uri.equals("/api/auth/join") || //회원가입
                uri.equals("/api/auth/reissue")) { //토큰 재발급
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String token = getToken(request);
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                System.out.println(token);
                System.out.println(jwtTokenProvider.validateToken(token));
                log.info("JWT 인증 실패: 토큰이 만료되거나 없음.");
                response.setStatus(ExceptionCode.UNAUTHORIZED.getStatus());
                return;
            }
        } catch (Exception e) {
            log.info("JWT 인증 실패: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            response.setStatus(ExceptionCode.FORBIDDEN.getStatus());
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("ACCESS_TOKEN".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}

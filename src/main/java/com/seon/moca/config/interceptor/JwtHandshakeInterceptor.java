package com.seon.moca.config.interceptor;

import com.seon.moca.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import java.util.Map;

@Slf4j
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtHandshakeInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        try {
            if (request instanceof ServletServerHttpRequest) {
                HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
                String token = extractTokenFromCookies(servletRequest.getCookies());

                if (token == null) {
                    log.warn("Handshake JWT 토큰 없음");
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return false;
                }

                if (!jwtTokenProvider.validateToken(token)) {
                    log.warn("Handshake JWT 토큰 유효하지 않음");
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return false;
                }

                String userId = jwtTokenProvider.getUserId(token);
                attributes.put("userId", userId);
                attributes.put("token", token); // 토큰도 저장해두기
                log.info("Handshake JWT 인증 성공 사용자 id: {}", userId);
                return true;
            }
        } catch (Exception e) {
            log.error("Handshake 중 예외 발생", e);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return false;
    }

    private String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("ACCESS_TOKEN".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        if (exception != null) {
            log.error("Handshake 완료 후 예외 발생", exception);
        } else {
            log.info("Handshake 성공적으로 완료");
        }
    }
}

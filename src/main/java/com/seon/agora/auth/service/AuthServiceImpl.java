package com.seon.agora.auth.service;

import com.seon.common.exception.ApiException;
import com.seon.common.exception.ExceptionCode;
import com.seon.common.util.IdGenerater;
import com.seon.agora.auth.dto.JoinRequest;
import com.seon.agora.auth.dto.LoginRequest;
import com.seon.agora.auth.repository.AuthRepository;
import com.seon.agora.common.security.UserInfo;
import com.seon.agora.user.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
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
    public boolean login(LoginRequest loginRequest, HttpServletRequest httpRequest) {
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

        //시큐리티 서버 세션
        UserInfo userInfo = new UserInfo(user);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userInfo, null, userInfo.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", context);

        log.info(user.getUserId() + "로그인");
        return true;
    }

    /**
     * 로그아웃
     */
    @Override
    public void logout(UserInfo user) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // JSESSIONID 쿠키 삭제
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // https 쓸거면 true
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);
        //로깅
        log.info(user.getId() + "로그아웃");
    }
}


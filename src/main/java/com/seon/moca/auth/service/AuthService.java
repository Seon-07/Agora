package com.seon.moca.auth.service;

import com.seon.moca.auth.dto.JoinRequest;
import com.seon.moca.auth.dto.LoginRequest;
import com.seon.moca.common.security.UserInfo;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-06
 */
public interface AuthService {
    void join(JoinRequest joinRequest);
    boolean login(LoginRequest loginRequest, HttpServletRequest httpRequest);
    void logout(UserInfo userInfo);
}

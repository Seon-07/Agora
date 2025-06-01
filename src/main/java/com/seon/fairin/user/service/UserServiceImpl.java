package com.seon.fairin.user.service;

import com.seon.fairin.auth.repository.AuthRepository;
import com.seon.fairin.jwt.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-23
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final AuthRepository authRepository;

    public String getUserNickname(UserInfo userInfo){
        return userInfo.getNickname();
    }
}

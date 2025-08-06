package com.seon.moca.user.service;

import com.seon.moca.jwt.UserInfo;
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

    public String getUserNickname(UserInfo userInfo){
        return userInfo.getNickname();
    }
}

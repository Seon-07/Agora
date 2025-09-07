package com.seon.moca.user.service;

import com.seon.moca.common.security.UserInfo;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-23
 */
public interface UserService {
    String getUserNickname(UserInfo userInfo);
}

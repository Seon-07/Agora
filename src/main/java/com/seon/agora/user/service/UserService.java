package com.seon.agora.user.service;

import com.seon.agora.common.security.UserInfo;
import com.seon.agora.user.dto.UserClientInfo;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-23
 */
public interface UserService {
    UserClientInfo getUserClientInfo(UserInfo userInfo);
}

package com.seon.moca.user.dto;

import com.seon.moca.common.security.UserInfo;
import lombok.Getter;

/**
 * @author seon
 * @version 1.0
 * @since 25. 9. 11.
 */
@Getter
public class UserClientInfo {
    private String id;

    private String nickname;

    public UserClientInfo(UserInfo userInfo){
        this.id = userInfo.getId();
        this.nickname = userInfo.getNickname();
    }
}

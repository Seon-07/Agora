package com.seon.moca.room.dto;

import com.seon.moca.common.security.UserInfo;
import lombok.Getter;

/**
 * @author seon
 * @version 1.0
 * @since 25. 9. 8.
 */
@Getter
public class RoomUserResponse {

    private String id;

    private String name;

    private String nickname;

    public RoomUserResponse(UserInfo userInfo) {
        this.id = userInfo.getId();
        this.name = userInfo.getName();
        this.nickname = userInfo.getNickname();
    }
}

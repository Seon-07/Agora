package com.seon.agora.room.dto;

import com.seon.agora.common.security.UserInfo;
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

    private String type;

    public RoomUserResponse(UserInfo userInfo, String type) {
        this.id = userInfo.getId();
        this.name = userInfo.getName();
        this.nickname = userInfo.getNickname();
        this.type = type;
    }
}

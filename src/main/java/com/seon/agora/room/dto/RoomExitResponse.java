package com.seon.agora.room.dto;

import lombok.Getter;

/**
 * @author seon
 * @version 1.0
 * @since 25. 9. 13.
 */
@Getter
public class RoomExitResponse {
    private String id;

    private String type;

    public RoomExitResponse(String id) {
        this.id = id;
        this.type = "delete";
    }
}

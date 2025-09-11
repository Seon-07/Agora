package com.seon.moca.room.dto;

import lombok.Getter;

/**
 * @author seon
 * @version 1.0
 * @since 25. 9. 11.
 */
@Getter
public class RoomExitRequest {
    private String roomId;

    private String userId;

    private DebateSide side;

    private String type;

    public RoomExitRequest(String roomId, String userId, DebateSide side) {
        this.roomId = roomId;
        this.userId = userId;
        this.side = side;
        this.type = "delete";
    }
}

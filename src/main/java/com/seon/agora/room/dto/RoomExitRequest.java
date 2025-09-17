package com.seon.agora.room.dto;

import com.seon.agora.debate.dto.DebateSide;
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

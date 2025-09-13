package com.seon.moca.room.dto;

import com.seon.moca.room.entity.Room;
import lombok.Getter;

/**
 * @author seon
 * @version 1.0
 * @since 25. 9. 13.
 */
@Getter
public class RoomStateResponse {
    private String id;

    private int round;

    private RoomStatus status;

    private String hostId;

    public RoomStateResponse(Room room, int round, RoomStatus status) {
        this.id = room.getId();
        this.round = round;
        this.status = status;
        this.hostId = room.getHost().getId();
    }
}

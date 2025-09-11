package com.seon.moca.room.dto;

import lombok.Getter;

/**
 * @author seon
 * @version 1.0
 * @since 25. 9. 7.
 */
@Getter
public class RoomCardResponse {
    private String id;

    private String name;

    private String topic;

    private RoomStatus status;

    private String type;

    public RoomCardResponse(RoomResponse roomResponse) {
        this.id = roomResponse.getId();
        this.name = roomResponse.getName();
        this.topic = roomResponse.getTopic();
        this.status = roomResponse.getStatus();
        this.type = "add";
     }
}

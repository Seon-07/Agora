package com.seon.agora.room.dto;

import lombok.Getter;

import java.util.List;

/**
 * @author seon
 * @version 1.0
 * @since 25. 9. 13.
 */
@Getter
public class RoomPaticipantsResponse {
    List<String> paticipantsList;

    public RoomPaticipantsResponse(List<String> paticipantsList) {
        this.paticipantsList = paticipantsList;
    }
}

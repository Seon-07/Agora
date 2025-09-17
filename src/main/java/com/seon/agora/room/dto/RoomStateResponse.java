package com.seon.agora.room.dto;

import com.seon.agora.chat.dto.SenderType;
import com.seon.agora.debate.dto.DebateActionType;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author seon
 * @version 1.0
 * @since 25. 9. 13.
 */
@Getter
@NoArgsConstructor
public class RoomStateResponse {
    private String id;

    private int round;

    private String pro;

    private String con;

    private SenderType speaker;

    private DebateActionType action;

    private RoomStatus status;

    public RoomStateResponse(String id, RoomStatus status) {
        this.id = id;
        this.status = status;
    }

    public RoomStateResponse debateInOut(String pro, String con){
        this.pro = pro;
        this.con = con;
        this.action = DebateActionType.WAIT;
        return this;
    }
}

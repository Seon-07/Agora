package com.seon.moca.room.service;

import com.seon.moca.common.security.UserInfo;
import com.seon.moca.room.dto.*;

import java.util.List;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-18
 */
public interface RoomService {
    RoomResponse createRoom(CreateRoomRequest createRoomRequest, UserInfo userInfo);

    List<RoomResponse> getRoomList(RoomStatus status);

    RoomResponse getRoom(String id, UserInfo userInfo);

    void exitRoom(RoomExitRequest roomExitRequest, UserInfo userInfo);

    RoomPaticipantsResponse getParticipants(String roomId, UserInfo userInfo);
}

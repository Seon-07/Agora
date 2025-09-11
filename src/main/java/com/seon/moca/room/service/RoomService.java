package com.seon.moca.room.service;

import com.seon.moca.common.security.UserInfo;
import com.seon.moca.room.dto.CreateRoomRequest;
import com.seon.moca.room.dto.RoomExitRequest;
import com.seon.moca.room.dto.RoomResponse;
import com.seon.moca.room.dto.RoomStatus;

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
}

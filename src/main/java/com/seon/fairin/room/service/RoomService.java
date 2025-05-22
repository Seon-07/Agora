package com.seon.fairin.room.service;

import com.seon.fairin.jwt.UserInfo;
import com.seon.fairin.room.dto.CreateRoomRequest;
import com.seon.fairin.room.dto.RoomResponse;
import com.seon.fairin.room.entity.RoomStatus;

import java.util.List;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-18
 */
public interface RoomService {
    void createRoom(CreateRoomRequest createRoomRequest, UserInfo userInfo);

    List<RoomResponse> getRoomList(RoomStatus status);
}

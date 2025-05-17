package com.seon.fairin.room.service;

import com.seon.fairin.jwt.UserInfo;
import com.seon.fairin.room.dto.CreateRoomRequest;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-18
 */
public interface RoomService {
    void createRoom(CreateRoomRequest createRoomRequest, UserInfo userInfo);
}

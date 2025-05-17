package com.seon.fairin.room.service;

import com.seon.common.util.IdGenerater;
import com.seon.fairin.jwt.UserInfo;
import com.seon.fairin.room.dto.CreateRoomRequest;
import com.seon.fairin.room.entity.Room;
import com.seon.fairin.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-18
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public void createRoom(CreateRoomRequest createRoomRequest, UserInfo userInfo) {
        Room room = Room.builder()
                .id(IdGenerater.generate())
                .name(createRoomRequest.getName())
                .des(createRoomRequest.getDes())
                .hostId(userInfo.getId())
                .isPrivate(createRoomRequest.isPrivate())
                .build();
        roomRepository.save(room);
    }
}

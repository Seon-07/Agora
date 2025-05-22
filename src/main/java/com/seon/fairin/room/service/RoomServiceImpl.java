package com.seon.fairin.room.service;

import com.seon.common.util.IdGenerater;
import com.seon.fairin.jwt.UserInfo;
import com.seon.fairin.room.dto.CreateRoomRequest;
import com.seon.fairin.room.dto.RoomResponse;
import com.seon.fairin.room.entity.Room;
import com.seon.fairin.room.entity.RoomStatus;
import com.seon.fairin.room.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public void createRoom(CreateRoomRequest createRoomRequest, UserInfo userInfo) {
        String id = IdGenerater.generate();
        Room room = Room.builder()
                .id(id)
                .name(createRoomRequest.getName())
                .des(createRoomRequest.getDes())
                .hostId(userInfo.getId())
                .isPrivate(createRoomRequest.isPrivate())
                .build();
        log.info("ROOM:" + room.getName() + " = " + id + "생성");
        roomRepository.save(room);
        redisTemplate.opsForValue().set("ROOM:" + id, room);
    }

    public List<RoomResponse> getRoomList(RoomStatus status) {
        List<Room> rooms = roomRepository.findByStatus(status);
        return rooms.stream()
                .map(this::toRoomResponse)
                .collect(Collectors.toList());
    }

    private RoomResponse toRoomResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .des(room.getDes())
                .hostId(room.getHostId())
                .opponentId(room.getOpponentId())
                .status(room.getStatus())
                .createDttm(room.getCreateDttm())
                .startDttm(room.getStartDttm())
                .endDttm(room.getEndDttm())
                .isPrivate(room.isPrivate())
                .build();
    }
}

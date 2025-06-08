package com.seon.fairin.room.service;

import com.seon.common.exception.ApiException;
import com.seon.common.exception.ExceptionCode;
import com.seon.common.util.IdGenerater;
import com.seon.fairin.gemini.dto.PromptDiv;
import com.seon.fairin.gemini.service.GeminiService;
import com.seon.fairin.jwt.UserInfo;
import com.seon.fairin.room.dto.CreateRoomRequest;
import com.seon.fairin.room.dto.RoomResponse;
import com.seon.fairin.room.dto.RoomStatus;
import com.seon.fairin.room.entity.Room;
import com.seon.fairin.room.repository.RoomRepository;
import com.seon.fairin.util.GeminiParser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
    private final GeminiService geminiService;
    private final RoomRepository roomRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public void createRoom(CreateRoomRequest createRoomRequest, UserInfo userInfo) {
        //주제 추출
        String topic = createRoomRequest.getTopic();
        //주제 검증
        String validResponse = geminiService.sendGemini(PromptDiv.validator, topic);
        String[] parsingResponse = GeminiParser.extractText(validResponse).split(":");
        String validResult = parsingResponse[0].trim();
        String validDes = parsingResponse[1].trim();
        System.out.println(validDes);
        if(validResult.equals("T")) {
            String id = IdGenerater.generate();
            Room room = Room.builder()
                    .id(id)
                    .name(createRoomRequest.getName())
                    .topic(validDes)
                    .hostId(userInfo.getId())
                    .isPrivate(createRoomRequest.isPrivate())
                    .build();
            roomRepository.save(room);
            //redisTemplate.opsForValue().set("ROOM:" + id, room, 1, TimeUnit.DAYS);
        }else if(validResult.equals("F")) {
            throw new ApiException(ExceptionCode.VALIDATION_ERROR, validDes);
        }else{
            log.warn("gemini validation error");
            throw new ApiException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }
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
                .topic(room.getTopic())
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

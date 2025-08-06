package com.seon.moca.room.service;

import com.seon.common.exception.ApiException;
import com.seon.common.exception.ExceptionCode;
import com.seon.common.util.IdGenerater;
import com.seon.moca.gemini.dto.PromptDiv;
import com.seon.moca.gemini.service.GeminiService;
import com.seon.moca.jwt.UserInfo;
import com.seon.moca.room.dto.CreateRoomRequest;
import com.seon.moca.room.dto.RoomResponse;
import com.seon.moca.room.dto.RoomStatus;
import com.seon.moca.room.entity.Room;
import com.seon.moca.room.repository.RoomRepository;
import com.seon.moca.util.GeminiParser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 채팅 방 생성
     */
    @Transactional
    public RoomResponse createRoom(CreateRoomRequest createRoomRequest, UserInfo userInfo) {
        //주제 추출
        String topic = createRoomRequest.getTopic();
        //주제 검증
        String validResponse = geminiService.sendGemini(PromptDiv.validator, topic);
        String[] parsingResponse = GeminiParser.extractText(validResponse).split(":");
        //주제 검증 T or F
        String validResult = parsingResponse[0].trim();
        //주제 검증 사유
        String validDes = parsingResponse[1].trim();

        if(validResult.equals("T")) {
            String id = IdGenerater.generate();
            Room room = Room.builder()
                    .id(id)
                    .name(createRoomRequest.getName())
                    .topic(validDes)
                    .hostId(userInfo.getId())
                    .isPrivate(createRoomRequest.isPrivate())
                    .build();
            RoomResponse roomResponse = toRoomResponse(roomRepository.save(room));
            messagingTemplate.convertAndSend("/topic/rooms", roomResponse);
            return roomResponse;
            //redisTemplate.opsForValue().set("ROOM:" + id, room, 1, TimeUnit.DAYS);
        }else if(validResult.equals("F")) {
            throw new ApiException(ExceptionCode.VALIDATION_ERROR, validDes);
        }else{
            log.warn("gemini validation error");
            throw new ApiException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 채팅방 상태에 따른 방 목록 가져오기
     */
    public List<RoomResponse> getRoomList(RoomStatus status) {
        List<Room> rooms = roomRepository.findByStatus(status);
        return rooms.stream()
                .map(this::toRoomResponse)
                .collect(Collectors.toList());
    }

    /**
     * 채팅방 접속
     */
    public RoomResponse getRoom(String id, UserInfo userInfo) {
        Optional<Room> room = roomRepository.findById(id);
        if(room.isEmpty()){
            throw new ApiException(ExceptionCode.NOT_FOUND);
        }
        return toRoomResponse(room.get());
    }
    /**
     * Entity -> DTO
     */
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

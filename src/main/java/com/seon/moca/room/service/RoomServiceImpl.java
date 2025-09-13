package com.seon.moca.room.service;

import com.seon.common.exception.ApiException;
import com.seon.common.exception.ExceptionCode;
import com.seon.common.util.IdGenerater;
import com.seon.moca.auth.repository.AuthRepository;
import com.seon.moca.common.security.UserInfo;
import com.seon.moca.common.service.RedisService;
import com.seon.moca.gemini.dto.PromptDiv;
import com.seon.moca.gemini.service.GeminiService;
import com.seon.moca.room.dto.*;
import com.seon.moca.room.entity.Room;
import com.seon.moca.room.repository.RoomRepository;
import com.seon.moca.user.entity.User;
import com.seon.moca.util.GeminiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final GeminiService geminiService;
    private final RoomRepository roomRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisService redisService;
    private final AuthRepository authRepository;

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

        //방생성자 찬반 구분
        DebateSide side = createRoomRequest.getSide();

        if(validResult.equals("T")) {
            String id = IdGenerater.generate();
            User host = authRepository.findById(userInfo.getId())
                    .orElseThrow(() -> new ApiException(ExceptionCode.USER_NOT_FOUND, userInfo.getId()));
            //채팅방 빌더
            Room room = Room.builder()
                    .id(id)
                    .name(createRoomRequest.getName())
                    .topic(validDes)
                    .host(host)
                    .isPrivate(createRoomRequest.isPrivate())
                    .build();
            //찬성 반대 분기처리
            if(side == DebateSide.PRO) {
                room.assignPro(host); // 찬성측일 경우 찬성에 아이디 주입
            }else{
                room.assignCon(host); // 반대측일 경우 반대에 아이디 주입
            }
            Room savedRoom = roomRepository.save(room);
            RoomResponse roomResponse = toRoomResponse(savedRoom);

            //새로운 방 발행
            messagingTemplate.convertAndSend("/topic/rooms", new RoomCardResponse(roomResponse));
            return roomResponse;
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
    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomList(RoomStatus status) {
        List<Room> rooms = roomRepository.findByStatusAndDelYn(status, false);
        return rooms.stream()
                .map(this::toRoomResponse)
                .collect(Collectors.toList());
    }

    /**
     * 채팅방 접속
     */
    @Transactional(readOnly = true)
    public RoomResponse getRoom(String id, UserInfo userInfo) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ApiException(ExceptionCode.NOT_FOUND, "존재하지 않는 방입니다."));
        //중복 제거
        List<String> current = redisService.getList("ROOM:" + id + ":participants", 0, -1);
        if (!current.contains(userInfo.getNickname())) {
            redisService.add("ROOM:" + id + ":participants", userInfo.getNickname());
        }
        //접속자 발행
        messagingTemplate.convertAndSend("/topic/room/" +room.getId()+"/users", new RoomUserResponse(userInfo, "add"));
        return toRoomResponse(room);
    }

    /**
     * 채팅방 퇴장
     */
    @Transactional
    public void exitRoom(RoomExitRequest roomExitRequest, UserInfo userInfo) {
        Room room = roomRepository.findById(roomExitRequest.getRoomId())
                .orElseThrow(() -> new ApiException(ExceptionCode.NOT_FOUND, "존재하지 않는 방입니다."));
        redisService.removeAll("ROOM:" + room.getId() + ":participants", userInfo.getNickname());
        if(roomExitRequest.getSide() ==  DebateSide.PRO) {
            room.assignPro(null);
        }else{
            room.assignCon(null);
        }
        //참가자 퇴장
        messagingTemplate.convertAndSend("/topic/room/" +room.getId()+"/users", new RoomUserResponse(userInfo, "delete"));
        //토론자 없으면 방 삭제
        if(room.getPro() == null && room.getCon() == null) {
            //소프트삭제
            room.softDelete();
            //방 삭제 발행
            messagingTemplate.convertAndSend("/topic/rooms", new RoomExitResponse(room.getId()));
            messagingTemplate.convertAndSend("/topic/room/" + room.getId()+"/state", new RoomStateResponse(room, 0, RoomStatus.EXIT));
            //레디스에서 제거
            redisService.delete("ROOM:" + room.getId() + ":participants");
        }
    }

    /**
     * 채팅방 접속자 조회
     */
    public RoomPaticipantsResponse getParticipants(String roomId, UserInfo userInfo) {
        String room = "ROOM:" + roomId + ":participants";
        List<String> participants = redisService.getList(room, 0, -1);
        return new RoomPaticipantsResponse(participants);
    }
    /**
     * Entity -> DTO
     */
    private RoomResponse toRoomResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .topic(room.getTopic())
                .hostNickname(room.getHost() == null ? null : room.getHost().getNickname())
                .proNickname(room.getPro() == null ? null : room.getPro().getNickname())
                .conNickname(room.getCon() == null ? null : room.getCon().getNickname())
                .status(room.getStatus())
                .createDttm(room.getCreateDttm())
                .startDttm(room.getStartDttm())
                .endDttm(room.getEndDttm())
                .isPrivate(room.isPrivate())
                .build();
    }
}

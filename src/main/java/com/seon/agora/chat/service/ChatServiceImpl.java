package com.seon.agora.chat.service;

import com.seon.common.exception.ApiException;
import com.seon.common.exception.ExceptionCode;
import com.seon.common.util.IdGenerater;
import com.seon.agora.auth.repository.AuthRepository;
import com.seon.agora.chat.dto.ChatRequest;
import com.seon.agora.chat.dto.ChatResponse;
import com.seon.agora.chat.dto.SenderType;
import com.seon.agora.chat.entity.Chat;
import com.seon.agora.chat.repository.ChatRepository;
import com.seon.agora.common.security.UserInfo;
import com.seon.agora.room.entity.Room;
import com.seon.agora.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author seon
 * @version 1.0
 * @since 25. 9. 9.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final RoomRepository roomRepository;
    private final AuthRepository authRepository;

    /**
     * 채팅 보내기
     */
    @Transactional
    public ChatResponse sendChat(ChatRequest chatRequest, UserInfo userInfo) {
        System.out.println(chatRequest.getMessage());
        //현재 대화 채팅방 조회
        Room room = roomRepository.findById(chatRequest.getRoomId())
                .orElseThrow(() -> new ApiException(ExceptionCode.NOT_FOUND, "해당 방이 존재하지 않습니다."));

        //메시지 전송자의 타입조회
        SenderType senderType = room.getPro().getId().equals(userInfo.getId()) ? SenderType.PRO : SenderType.CON;

        //엔티티 객체 생성
        Chat chat = Chat.builder()
                        .id(IdGenerater.generate())
                        .room(room)
                        .message(chatRequest.getMessage())
                        .sender(authRepository.getReferenceById(userInfo.getId()))
                        .senderType(senderType)
                        .build();
        //저장
        chatRepository.save(chat);

        //반환 객체 생성
        ChatResponse chatResponse = new ChatResponse(chat, userInfo.getNickname());

        //메시지 발행
        messagingTemplate.convertAndSend("/topic/room/" +chatRequest.getRoomId()+"/chat", chatResponse);
        return chatResponse;
    }
}

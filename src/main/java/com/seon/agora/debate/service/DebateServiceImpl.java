package com.seon.agora.debate.service;

import com.seon.common.exception.ApiException;
import com.seon.common.exception.ExceptionCode;
import com.seon.agora.auth.repository.AuthRepository;
import com.seon.agora.common.security.UserInfo;
import com.seon.agora.debate.dto.DebateParticipationRequest;
import com.seon.agora.debate.dto.DebateSide;
import com.seon.agora.room.dto.*;
import com.seon.agora.room.entity.Room;
import com.seon.agora.room.repository.RoomRepository;
import com.seon.agora.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-18
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DebateServiceImpl implements DebateService {
    private final RoomRepository roomRepository;
    private final AuthRepository authRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 토론 참여
     */
    @Transactional
    public void debateParticipation(DebateParticipationRequest debateParticipationRequest, UserInfo userInfo) {
        Room room = roomRepository.findById(debateParticipationRequest.getRoomId())
                .orElseThrow(() -> new ApiException(ExceptionCode.NOT_FOUND, "존재하지 않는 방입니다."));
        User user = authRepository.findById(userInfo.getId())
                .orElseThrow(() -> new ApiException(ExceptionCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));
        if(debateParticipationRequest.isDebateIn()){
            if(debateParticipationRequest.getSide() == DebateSide.PRO){
                room.assignPro(user);
            }else{
                room.assignCon(user);
            }
        }else{
            if(debateParticipationRequest.getSide() == DebateSide.PRO){
                room.assignPro(null);
            }else{
                room.assignCon(null);
            }
        }
        String proNickname = room.getPro().getNickname();
        String conNickname = room.getCon().getNickname();
        messagingTemplate.convertAndSend("/topic/room/" + room.getId()+"/state", new RoomStateResponse().debateInOut(proNickname, conNickname));
    }
}

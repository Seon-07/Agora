package com.seon.agora.debate.service;

import com.seon.agora.common.security.UserInfo;
import com.seon.agora.debate.dto.DebateParticipationRequest;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-18
 */
public interface DebateService {
    void debateParticipation(DebateParticipationRequest debateParticipationRequest, UserInfo userInfo);
}

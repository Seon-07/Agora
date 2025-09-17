package com.seon.agora.debate.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-09-14
 */
@Getter
@Setter
public class DebateParticipationRequest {

    private String userid;

    private String roomId;

    private DebateSide side;

    private boolean debateIn;
}

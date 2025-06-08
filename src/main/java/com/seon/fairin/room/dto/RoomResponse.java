package com.seon.fairin.room.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-18
 */
@Getter
@Builder
public class RoomResponse {
    private String id;

    private String name;

    private String topic;

    private String hostId;

    private String opponentId;

    private RoomStatus status;

    private LocalDateTime createDttm;

    private LocalDateTime startDttm;

    private LocalDateTime endDttm;

    private boolean isPrivate;
}

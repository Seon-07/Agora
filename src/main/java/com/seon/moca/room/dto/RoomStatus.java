package com.seon.moca.room.dto;

import lombok.Getter;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-17
 */
@Getter
public enum RoomStatus {
    WAITING("대기중"),
    ONGOING("진행중"),
    CLOSED("종료"),
    EXIT("강제종료");

    private final String des;

    RoomStatus(String des) {
        this.des = des;
    }
}

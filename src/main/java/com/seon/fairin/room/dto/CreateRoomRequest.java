package com.seon.fairin.room.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-18
 */
@Getter
@Setter
public class CreateRoomRequest {

    @NotBlank(message = "방 제목을 입력해주세요.")
    private String name;

    private String topic;

    private boolean isPrivate;
}

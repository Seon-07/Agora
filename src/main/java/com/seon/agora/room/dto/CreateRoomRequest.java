package com.seon.agora.room.dto;

import com.seon.agora.debate.dto.DebateSide;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "주제를 입력해주세요.")
    private String topic;

    @NotNull(message = "찬성 또는 반대를 선택해주세요.")
    private DebateSide side;

    private boolean isPrivate;
}

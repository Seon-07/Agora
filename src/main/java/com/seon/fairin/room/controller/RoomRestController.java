package com.seon.fairin.room.controller;

import com.seon.common.response.ApiResponse;
import com.seon.common.response.DataResult;
import com.seon.fairin.jwt.UserInfo;
import com.seon.fairin.room.dto.CreateRoomRequest;
import com.seon.fairin.room.dto.RoomStatus;
import com.seon.fairin.room.service.RoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-18
 */
@Tag(name = "채팅방", description = "채팅방 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class RoomRestController {
    private final RoomService roomService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createRoom(@RequestBody @Valid CreateRoomRequest createRoomRequest, @AuthenticationPrincipal UserInfo user) {
        ApiResponse responseBody = DataResult.success(roomService.createRoom(createRoomRequest, user), "방 생성 성공");
        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getRoom(@RequestParam String id, @AuthenticationPrincipal UserInfo user) {
        ApiResponse responseBody = DataResult.success(roomService.getRoom(id, user), "방 입장 성공");
        return ResponseEntity.ok().body(responseBody);
    }


    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getRoomList(@RequestParam RoomStatus status){
        ApiResponse responseBody = DataResult.success(roomService.getRoomList(status));
        return ResponseEntity.ok().body(responseBody);
    }
}

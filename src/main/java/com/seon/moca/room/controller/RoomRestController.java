package com.seon.moca.room.controller;

import com.seon.common.response.ApiResponse;
import com.seon.common.response.DataResult;
import com.seon.common.response.OperationResult;
import com.seon.moca.common.security.UserInfo;
import com.seon.moca.room.dto.CreateRoomRequest;
import com.seon.moca.room.dto.RoomExitRequest;
import com.seon.moca.room.dto.RoomStatus;
import com.seon.moca.room.service.RoomService;
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
    public ResponseEntity<ApiResponse> createRoom(@RequestBody @Valid CreateRoomRequest createRoomRequest, @AuthenticationPrincipal UserInfo userInfo) {
        ApiResponse responseBody = DataResult.success(roomService.createRoom(createRoomRequest, userInfo), "방 생성 성공");
        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getRoom(@RequestParam String id, @AuthenticationPrincipal UserInfo userInfo) {
        ApiResponse responseBody = DataResult.success(roomService.getRoom(id, userInfo), "방 입장 성공");
        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping("/exit")
    public ResponseEntity<ApiResponse> exitRoom(@RequestBody RoomExitRequest roomExitRequest, @AuthenticationPrincipal UserInfo userInfo) {
        roomService.exitRoom(roomExitRequest, userInfo);
        ApiResponse responseBody = OperationResult.success("방 퇴장");
        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getRoomList(@RequestParam RoomStatus status){
        ApiResponse responseBody = DataResult.success(roomService.getRoomList(status));
        return ResponseEntity.ok().body(responseBody);
    }
}

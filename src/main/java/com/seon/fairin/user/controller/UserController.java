package com.seon.fairin.user.controller;

import com.seon.common.response.ApiResponse;
import com.seon.common.response.DataResult;
import com.seon.fairin.jwt.UserInfo;
import com.seon.fairin.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-05-23
 */
@Tag(name = "사용자", description = "사용자 정보 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/nickname")
    public ResponseEntity<ApiResponse> getUserName(@AuthenticationPrincipal UserInfo user){
        ApiResponse responseBody = DataResult.success(userService.getUserNickname(user));
        return ResponseEntity.ok().body(responseBody);
    }
}

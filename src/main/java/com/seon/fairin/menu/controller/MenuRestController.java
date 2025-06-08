package com.seon.fairin.menu.controller;

import com.seon.common.response.ApiResponse;
import com.seon.common.response.DataResult;
import com.seon.fairin.jwt.UserInfo;
import com.seon.fairin.menu.service.MenuService;
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
 * @since 2025-06-08
 */
@Tag(name = "메뉴", description = "메뉴 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menu")
public class MenuRestController {

    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<ApiResponse> getMenuList(@AuthenticationPrincipal UserInfo user){
        ApiResponse responseBody = DataResult.success(menuService.getMeusList(user));
        return ResponseEntity.ok().body(responseBody);
    }
}

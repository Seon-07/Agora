package com.seon.agora.debate.controller;

import com.seon.common.response.ApiResponse;
import com.seon.common.response.OperationResult;
import com.seon.agora.common.security.UserInfo;
import com.seon.agora.debate.dto.DebateParticipationRequest;
import com.seon.agora.debate.service.DebateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-09-14
 */
@Tag(name = "토론", description = "토론 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/debate")
public class DebateRestController {
    private final DebateService debateService;

    @PostMapping("/participation")
    public ResponseEntity<ApiResponse> debateParticipation(@RequestBody DebateParticipationRequest debateParticipationRequest, @AuthenticationPrincipal UserInfo userInfo) {
        debateService.debateParticipation(debateParticipationRequest, userInfo);
        ApiResponse responseBody = OperationResult.success("토론 참여");
        return ResponseEntity.ok().body(responseBody);
    }
}

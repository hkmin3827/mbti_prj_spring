package com.whatslovermbti.mbti_prj.controller.action;

import com.whatslovermbti.mbti_prj.constant.ActionType;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.security.auth.CustomUserDetails;
import com.whatslovermbti.mbti_prj.service.CurrentUserService;
import com.whatslovermbti.mbti_prj.service.PlaceReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reactions")
public class PlaceReactionController {

    private final CurrentUserService currentUserService;
    private final PlaceReactionService placeReactionService;

    @PostMapping("/places/{placeId}")
    public void react(
            @PathVariable Long placeId,
            @RequestParam ActionType type,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = currentUserService.getCurrentUser(userDetails);
        placeReactionService.react(user.getId(), placeId, type);
    }
}
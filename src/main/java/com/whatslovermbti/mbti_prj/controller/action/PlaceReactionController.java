package com.whatslovermbti.mbti_prj.controller.action;

import com.whatslovermbti.mbti_prj.annotation.LoginUser;
import com.whatslovermbti.mbti_prj.constant.ActionType;
import com.whatslovermbti.mbti_prj.service.PlaceReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reactions")
public class PlaceReactionController {

    private final PlaceReactionService placeReactionService;

    @PostMapping("/places/{placeId}")
    public void react(
            @PathVariable Long placeId,
            @RequestParam ActionType type,
            @LoginUser Long userId
    ) {
        placeReactionService.react(userId, placeId, type);
    }
}
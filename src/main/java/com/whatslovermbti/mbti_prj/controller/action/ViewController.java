package com.whatslovermbti.mbti_prj.controller.action;

import com.whatslovermbti.mbti_prj.annotation.LoginUser;
import com.whatslovermbti.mbti_prj.service.CurrentUserService;
import com.whatslovermbti.mbti_prj.service.UserActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/views")
class ViewController {

    private final CurrentUserService currentUserService;
    private final UserActionService userActionService;

    @PostMapping("/places/{placeId}")
    public void viewPlace(
            @PathVariable Long placeId,
            @LoginUser Long userId
    ) {
        userActionService.recordView(userId, placeId);
    }
}
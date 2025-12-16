package com.whatslovermbti.mbti_prj.controller.action;

import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.security.auth.CustomUserDetails;
import com.whatslovermbti.mbti_prj.service.CurrentUserService;
import com.whatslovermbti.mbti_prj.service.UserActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = currentUserService.getCurrentUser(userDetails);
        userActionService.recordView(user.getId(), placeId);
    }
}
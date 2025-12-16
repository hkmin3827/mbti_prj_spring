package com.whatslovermbti.mbti_prj.controller.action;

import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.security.auth.CustomUserDetails;
import com.whatslovermbti.mbti_prj.service.CurrentUserService;
import com.whatslovermbti.mbti_prj.service.UserActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final CurrentUserService currentUserService;
    private final UserActionService userActionService;

    @PostMapping("/places/{placeId}")
    public void save(
            @PathVariable Long placeId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = currentUserService.getCurrentUser(userDetails);
        userActionService.bookmarkPlace(user.getId(), placeId);
    }

    @DeleteMapping("/places/{placeId}")
    public void unsave(
            @PathVariable Long placeId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = currentUserService.getCurrentUser(userDetails);
        userActionService.removeBookmark(user.getId(), placeId);
    }
}
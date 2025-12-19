package com.whatslovermbti.mbti_prj.controller.action;

import com.whatslovermbti.mbti_prj.annotation.LoginUser;
import com.whatslovermbti.mbti_prj.service.CurrentUserService;
import com.whatslovermbti.mbti_prj.service.UserActionService;
import lombok.RequiredArgsConstructor;
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
            @LoginUser Long userId
    ) {
        userActionService.bookmarkPlace(userId, placeId);
    }

    @DeleteMapping("/places/{placeId}")
    public void unsave(
            @PathVariable Long placeId,
            @LoginUser Long userId
    ) {
        userActionService.removeBookmark(userId, placeId);
    }
}
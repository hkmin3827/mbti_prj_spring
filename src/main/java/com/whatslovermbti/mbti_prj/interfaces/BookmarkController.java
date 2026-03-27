package com.whatslovermbti.mbti_prj.interfaces;

import com.whatslovermbti.mbti_prj.global.annotation.LoginUser;
import com.whatslovermbti.mbti_prj.global.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.domain.place.dto.PlaceResDto;
import com.whatslovermbti.mbti_prj.domain.user.entity.User;
import com.whatslovermbti.mbti_prj.global.security.auth.CustomUserDetails;
import com.whatslovermbti.mbti_prj.application.action.UserActionQueryService;
import com.whatslovermbti.mbti_prj.application.action.UserActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final UserActionService userActionService;
    private final UserActionQueryService userActionQueryService;

    @PostMapping("/places/{placeId}")
    public void saveBookmark(
            @PathVariable Long placeId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "SELF") MbtiContext context
    ) {
        User user = userDetails.getUser();

        userActionService.bookmarkPlace(user.getId(), placeId, context);
    }

    @DeleteMapping("/places/{placeId}")
    public void unsaveBookmark(
            @PathVariable Long placeId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();

        userActionService.removeBookmark(user.getId(), placeId);
    }

    @GetMapping
    public List<PlaceResDto> myBookmarks(
            @LoginUser Long userId
    ) {
        return userActionQueryService.getBookmarks(userId);
    }

}
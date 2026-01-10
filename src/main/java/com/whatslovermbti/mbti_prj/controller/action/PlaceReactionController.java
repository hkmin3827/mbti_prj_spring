package com.whatslovermbti.mbti_prj.controller.action;

import com.whatslovermbti.mbti_prj.annotation.LoginUser;
import com.whatslovermbti.mbti_prj.constant.ActionType;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.dto.place.PlaceResDto;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.security.auth.CustomUserDetails;
import com.whatslovermbti.mbti_prj.service.action.PlaceReactionService;
import com.whatslovermbti.mbti_prj.service.action.UserActionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reactions")
public class PlaceReactionController {

    private final PlaceReactionService placeReactionService;
    private final UserActionQueryService userActionQueryService;

    @PostMapping("/places/{placeId}")
    public void react(
            @PathVariable Long placeId,
            @RequestParam ActionType type,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "SELF") MbtiContext context
    ) {
        User user = userDetails.getUser();
        placeReactionService.react(user.getId(), placeId, type, context);
    }

    @DeleteMapping("/places/{placeId}")
    public ResponseEntity<Void> removeReaction(
            @PathVariable Long placeId,
            @RequestParam ActionType type,
            @RequestParam(defaultValue = "SELF") MbtiContext context,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        placeReactionService.removeReaction(
                userDetails.getUser().getId(),
                placeId,
                type,
                context
        );
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<PlaceResDto> myLikedPlaces(
            @LoginUser Long userId
    ) {
        return userActionQueryService.getLikedPlaces(userId);
    }
}
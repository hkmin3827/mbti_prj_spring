package com.whatslovermbti.mbti_prj.interfaces;

import com.whatslovermbti.mbti_prj.global.annotation.LoginUser;
import com.whatslovermbti.mbti_prj.global.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.domain.place.dto.PlaceResDto;
import com.whatslovermbti.mbti_prj.domain.place.dto.PlaceSnapshot;
import com.whatslovermbti.mbti_prj.domain.action.view.dto.PlaceViewCountDto;
import com.whatslovermbti.mbti_prj.domain.action.view.dto.PlaceViewResponse;
import com.whatslovermbti.mbti_prj.domain.place.entity.Place;
import com.whatslovermbti.mbti_prj.domain.user.entity.User;
import com.whatslovermbti.mbti_prj.global.security.auth.CustomUserDetails;
import com.whatslovermbti.mbti_prj.application.action.UserActionQueryService;
import com.whatslovermbti.mbti_prj.application.action.UserActionService;
import com.whatslovermbti.mbti_prj.application.place.PlaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/views")
class ViewController {
    private final UserActionService userActionService;
    private final PlaceMapper placeMapper;
    private final UserActionQueryService userActionQueryService;

    @PostMapping("/place")
    public PlaceViewResponse viewPlace(
            @RequestBody PlaceSnapshot snapshot,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "SELF") MbtiContext context
    ) {
        User user = userDetails.getUser();

        Place place = userActionService.onPlaceClicked(
                user,
                snapshot,
                context
        );

        return new PlaceViewResponse(place.getId());
    }

    @GetMapping("/me")
    public List<PlaceResDto> myViews(
            @LoginUser Long userId
    ) {
        return userActionQueryService.getRecentViews(userId, placeMapper);
    }

    @DeleteMapping("/{placeId}")
    public ResponseEntity<Void> removeViewHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long placeId
    ) {
        userActionService.removeViewHistory(
                userDetails.getUser().getId(),
                placeId
        );
        return ResponseEntity.noContent().build(); // 204
    }

    @GetMapping("/most-viewed")
    public List<PlaceViewCountDto> getMostViewedPlaces(
            @RequestParam String mbti,
            @RequestParam(defaultValue = "5") int limit
    ) {
        return userActionQueryService
                .getMostViewedPlacesByMbti(mbti, limit);
    }
}
package com.whatslovermbti.mbti_prj.controller.action;

import com.whatslovermbti.mbti_prj.annotation.LoginUser;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.dto.place.PlaceResDto;
import com.whatslovermbti.mbti_prj.dto.place.PlaceSnapshot;
import com.whatslovermbti.mbti_prj.dto.place.PlaceViewCountDto;
import com.whatslovermbti.mbti_prj.dto.place.PlaceViewResponse;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.security.auth.CustomUserDetails;
import com.whatslovermbti.mbti_prj.service.action.UserActionQueryService;
import com.whatslovermbti.mbti_prj.service.action.UserActionService;
import com.whatslovermbti.mbti_prj.service.place.PlaceMapper;
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
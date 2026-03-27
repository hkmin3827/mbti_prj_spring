package com.whatslovermbti.mbti_prj.interfaces;

import com.whatslovermbti.mbti_prj.global.constant.Category;
import com.whatslovermbti.mbti_prj.global.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.domain.place.dto.PlaceDetailResponse;
import com.whatslovermbti.mbti_prj.domain.review.dto.ReviewResponse;
import com.whatslovermbti.mbti_prj.domain.user.entity.User;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
import com.whatslovermbti.mbti_prj.global.security.auth.CustomUserDetails;
import com.whatslovermbti.mbti_prj.application.place.PlaceSearchService;
import com.whatslovermbti.mbti_prj.application.place.PlaceService;
import com.whatslovermbti.mbti_prj.application.review.ReviewQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceSearchService placeSearchService;
    private final PlaceService placeService;
    private final ReviewQueryService reviewQueryService;

    @GetMapping("/detail")
    public PlaceDetailResponse getDetails(
            @RequestParam Long placeId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "SELF") MbtiContext context
    ) {
        User user = userDetails.getUser();

        return placeService.getPlaceDetail(placeId, user.getId(), context);
    }

    @GetMapping("/{placeId}/reviews")
    public Page<ReviewResponse> getPlaceReviews(
            @PathVariable Long placeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return reviewQueryService.getReviewsByPlace(placeId, page, size);
    }

    @GetMapping("/search")
    public KakaoMapResponse recommend(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "SELF") MbtiContext context,
            @RequestParam(defaultValue = "37.4979") double lat,
            @RequestParam(defaultValue = "127.0276") double lng,
            @RequestParam(required = false) Category category,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "2000") int radius
    ) {
        User user = userDetails.getUser();

        Category resolvedCategory =
                category != null ? category : Category.CAFE;

        return placeSearchService.search(
                user,
                context,
                lat,
                lng,
                radius,
                resolvedCategory,
                size
        );
    }
}
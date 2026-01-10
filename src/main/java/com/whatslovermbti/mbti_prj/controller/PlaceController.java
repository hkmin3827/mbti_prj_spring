package com.whatslovermbti.mbti_prj.controller;

import com.whatslovermbti.mbti_prj.constant.Category;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.dto.place.PlaceDetailResponse;
import com.whatslovermbti.mbti_prj.dto.review.ReviewResponse;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
import com.whatslovermbti.mbti_prj.security.auth.CustomUserDetails;
import com.whatslovermbti.mbti_prj.service.place.PlaceSearchService;
import com.whatslovermbti.mbti_prj.service.place.PlaceService;
import com.whatslovermbti.mbti_prj.service.review.ReviewQueryService;
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
    /**
     * 통합 장소 검색
     * - 위치 필수
     * - category 선택
     */
    @GetMapping("/search")
    public KakaoMapResponse recommend(
            @AuthenticationPrincipal CustomUserDetails userDetails, // 핵심
            @RequestParam(defaultValue = "SELF") MbtiContext context,
            @RequestParam(defaultValue = "37.4979") double lat,
            @RequestParam(defaultValue = "127.0276") double lng,
            @RequestParam(required = false) Category category,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "2000") int radius
    ) {
        // 디폴트 위치 : 강남역, 카테고리 : 카페

        User user = userDetails.getUser();

        Category resolvedCategory =
                category != null ? category : Category.CAFE;

        // 추천 검색
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
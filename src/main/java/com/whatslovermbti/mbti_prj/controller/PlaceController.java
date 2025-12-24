package com.whatslovermbti.mbti_prj.controller;

import com.whatslovermbti.mbti_prj.constant.Category;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
import com.whatslovermbti.mbti_prj.security.auth.CustomUserDetails;
import com.whatslovermbti.mbti_prj.service.place.PlaceSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceSearchService placeSearchService;

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
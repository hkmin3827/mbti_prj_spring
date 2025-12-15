package com.whatslovermbti.mbti_prj.controller;

import com.whatslovermbti.mbti_prj.constant.Category;
import com.whatslovermbti.mbti_prj.dto.place.PlaceResDto;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapClient;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
import com.whatslovermbti.mbti_prj.infra.kakao.dto.KakaoKeywordResponse;
import com.whatslovermbti.mbti_prj.repository.UserRepository;
import com.whatslovermbti.mbti_prj.security.auth.CustomUserDetails;
import com.whatslovermbti.mbti_prj.service.place.PlaceSearchService;
import com.whatslovermbti.mbti_prj.service.recommendation.PlaceRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceSearchService placeSearchService;
    private final UserRepository userRepository;

    /**
     * 통합 장소 검색
     * - 위치 필수
     * - keyword / category 선택
     */
    @GetMapping("/search")
    public KakaoMapResponse search(
            @AuthenticationPrincipal CustomUserDetails userDetails, // 🔥 핵심
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "1500") int radius,
            @RequestParam(required = false) String categoryCode,
            @RequestParam(defaultValue = "20") int size
    ) {
        // 1️⃣ JWT에서 userId 복원
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        // 2️⃣ 추천 검색
        return placeSearchService.search(
                user,
                lat,
                lng,
                radius,
                categoryCode,
                size
        );
    }
}
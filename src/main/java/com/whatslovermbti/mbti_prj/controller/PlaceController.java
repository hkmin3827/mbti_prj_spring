package com.whatslovermbti.mbti_prj.controller;

import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
import com.whatslovermbti.mbti_prj.resolver.TargetMbtiResolver;
import com.whatslovermbti.mbti_prj.security.auth.CustomUserDetails;
import com.whatslovermbti.mbti_prj.service.CurrentUserService;
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
    private final CurrentUserService currentUserService;
    private final TargetMbtiResolver targetMbtiResolver;

    /**
     * 통합 장소 검색
     * - 위치 필수
     * - keyword / category 선택
     */
    @GetMapping("/search")
    public KakaoMapResponse recommend(
            @AuthenticationPrincipal CustomUserDetails userDetails, // 핵심
            @RequestParam(defaultValue = "SELF") MbtiContext targetMbti,
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(required = false) String categoryCode,
            @RequestParam(defaultValue = "20") int size
    ) {
        User user = currentUserService.getCurrentUser(userDetails);
        String resolvedMbti =
                targetMbtiResolver.resolve(user, targetMbti);
        // 2️⃣ 추천 검색
        return placeSearchService.search(
                user,
                resolvedMbti,
                lat,
                lng,
                categoryCode,
                size
        );
    }
}
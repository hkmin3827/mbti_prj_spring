package com.whatslovermbti.mbti_prj.controller;


import com.whatslovermbti.mbti_prj.constant.Category;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
import com.whatslovermbti.mbti_prj.service.place.PlaceCandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test/places")
@RequiredArgsConstructor
public class PlaceCandidateTestController {

    private final PlaceCandidateService placeCandidateService;

    /**
     * 카카오 카테고리 페이지네이션 테스트
     * ex: /test/places/candidates?lat=37.5665&lng=126.9780&category=CE7
     */
    @GetMapping("/candidates")
    public CandidateTestResponse testCandidates(
            @RequestParam(defaultValue = "37.4979") double lat,
            @RequestParam(defaultValue = "127.0276") double lng,
            @RequestParam(required = false) Category category,
            @RequestParam(defaultValue = "2000") int radius
    ) {
        // 디폴트 위도, 경도 위치 : 강남역

        Category resolvedCategory =
                category != null ? category : Category.CAFE;

        List<KakaoMapResponse.Document> candidates =
                placeCandidateService.fetchCandidates(
                        lat,
                        lng,
                        radius,
                        resolvedCategory
                );

        return new CandidateTestResponse(
                candidates.size(),
                candidates
        );
    }

    /**
     * 테스트용 응답 DTO
     */
    public record CandidateTestResponse(
            int count,
            List<KakaoMapResponse.Document> documents
    ) {}
}
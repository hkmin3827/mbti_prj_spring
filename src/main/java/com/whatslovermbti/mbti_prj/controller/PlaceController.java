package com.whatslovermbti.mbti_prj.controller;

import com.whatslovermbti.mbti_prj.constant.Category;
import com.whatslovermbti.mbti_prj.dto.place.PlaceResDto;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapClient;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
import com.whatslovermbti.mbti_prj.infra.kakao.dto.KakaoKeywordResponse;
import com.whatslovermbti.mbti_prj.service.place.PlaceSearchService;
import com.whatslovermbti.mbti_prj.service.recommendation.PlaceRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceRecommendationService placeService;
    private final PlaceSearchService placeSearchService;
    private final KakaoMapClient kakaoMapClient;

//    @GetMapping("/nearby")
//    public List<PlaceResDto> nearby(
//            @RequestParam double lat,
//            @RequestParam double lng,
//
//            // ✅ 장소 종류를 명시적으로 받는다
//            @RequestParam Category category,
//
//            // ✅ 기본값 + 1차 방어
//            @RequestParam(defaultValue = "1000") int radius
//    ) {
//        return placeService.recommendNearby(
//                lat,
//                lng,
//                radius,
//                category
//        );
//    }
    @GetMapping("/search")
    public KakaoKeywordResponse search(@RequestParam String keyword,
                                       @RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return placeSearchService.searchByKeyword(keyword, page, size);
    }

    @GetMapping("/nearby")
    public KakaoMapResponse nearby(@RequestParam double lat,
                                   @RequestParam double lng,
                                   @RequestParam(defaultValue = "500") int radius,
                                   @RequestParam(defaultValue = "FD6") String categoryCode) {
        return placeSearchService.searchNearby(lat, lng, radius, categoryCode);
    }
}
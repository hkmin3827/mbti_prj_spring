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


    @GetMapping("/candidates")
    public CandidateTestResponse testCandidates(
            @RequestParam(defaultValue = "37.4979") double lat,
            @RequestParam(defaultValue = "127.0276") double lng,
            @RequestParam(required = false) Category category,
            @RequestParam(defaultValue = "2000") int radius
    ) {
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

    public record CandidateTestResponse(
            int count,
            List<KakaoMapResponse.Document> documents
    ) {}
}
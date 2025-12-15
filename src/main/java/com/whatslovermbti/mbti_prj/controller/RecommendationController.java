package com.whatslovermbti.mbti_prj.controller;


import com.whatslovermbti.mbti_prj.annotation.LoginUser;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.service.recommendation.PlaceRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
public class RecommendationController {

    private final PlaceRecommendationService placeRecommendationService;

//    @GetMapping("/places")
//    public List<Place> recommendPlaces(
//            @LoginUser User user,
//            @RequestParam MbtiContext context,
//            @RequestParam(defaultValue = "20") int limit
//    ) {
//        return placeRecommendationService.recommendPlaces(user, context, limit);
//    }
}
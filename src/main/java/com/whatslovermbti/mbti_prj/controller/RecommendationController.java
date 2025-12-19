//package com.whatslovermbti.mbti_prj.controller;
//
//
//import com.whatslovermbti.mbti_prj.annotation.LoginUser;
//import com.whatslovermbti.mbti_prj.constant.MbtiContext;
//import com.whatslovermbti.mbti_prj.dto.place.PlaceResDto;
//import com.whatslovermbti.mbti_prj.entity.Place;
//import com.whatslovermbti.mbti_prj.entity.User;
//import com.whatslovermbti.mbti_prj.resolver.TargetMbtiResolver;
//import com.whatslovermbti.mbti_prj.security.auth.CustomUserDetails;
//import com.whatslovermbti.mbti_prj.service.CurrentUserService;
//import com.whatslovermbti.mbti_prj.service.recommendation.PlaceRecommendationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Currency;
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/recommend")
//public class RecommendationController {
//
//    private final PlaceRecommendationService placeRecommendationService;
//    private final CurrentUserService currentUserService;
//    private final TargetMbtiResolver targetMbtiResolver;
//
//    @GetMapping
//    public List<PlaceResDto> recommend(
//            @AuthenticationPrincipal CustomUserDetails userDetails,
//            @RequestParam(defaultValue = "SELF") MbtiContext targetMbti,
//            @RequestParam double lat,
//            @RequestParam double lng
//    ) {
//        User user = currentUserService.getCurrentUser(userDetails);
//
//        String resolvedMbti =
//                targetMbtiResolver.resolve(user, targetMbti);
//
//        // ⭐ controller는 여기까지만
//        return placeSearch.recommendFromCandidates(
//                user,
//                resolvedMbti,
//                lat,
//                lng,
//                10
//        );
//    }
//}
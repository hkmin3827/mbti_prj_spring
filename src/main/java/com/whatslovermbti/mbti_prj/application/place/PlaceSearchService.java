package com.whatslovermbti.mbti_prj.application.place;

import com.whatslovermbti.mbti_prj.global.constant.Category;
import com.whatslovermbti.mbti_prj.global.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.domain.user.entity.User;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
import com.whatslovermbti.mbti_prj.application.recommendation.PlaceRecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlaceSearchService {
    private final PlaceCandidateService placeCandidateService;
    private final PlaceRecommendationService placeRecommendationService;

    public KakaoMapResponse search(
            User user,
            MbtiContext context,
            double lat,
            double lng,
            int radius,
            Category category,
            int size
    ) {

        List<KakaoMapResponse.Document> candidates =
                placeCandidateService.fetchCandidates(
                        lat,
                        lng,
                        radius,
                        category
                );

        List<KakaoMapResponse.Document> picked =
                placeRecommendationService.recommendFromCandidates(
                        user,
                        context,
                        candidates,
                        size
                );

        KakaoMapResponse response = new KakaoMapResponse();
        response.applyDocuments(picked);
        return response;
    }
}
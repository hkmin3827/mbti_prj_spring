package com.whatslovermbti.mbti_prj.service.place;

import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapClient;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse.Document;
import com.whatslovermbti.mbti_prj.service.KeywordWeightAggregator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceCandidateService {
    private static final int KEYWORD_LIMIT = 5;   // 상위 키워드 수
    private static final int PAGE_SIZE = 15;      // 키워드당 조회 수

    private final KakaoMapClient kakaoMapClient;
    private final KeywordWeightAggregator keywordWeightAggregator;

    @Cacheable(
            value = "kakaoCandidateCache",
            key = "'candidate:' + #user.id + ':' + #user.mbti + ':' + #lat + ':' + #lng + ':' + #radius + ':' + #categoryCode"
    )
    public KakaoMapResponse searchCandidates(
            User user,
            double lat,
            double lng,
            int radius,
            String categoryCode
    ) {
        log.info("Kakao API CALL (CANDIDATE - CATEGORY + OPTIONAL KEYWORD)");

        Map<String, KakaoMapResponse.Document> merged = new LinkedHashMap<>();

        /* ============================
           1️⃣ category 기반 1차 후보군
           ============================ */
        KakaoMapResponse categoryResp =
                kakaoMapClient.searchByCategory(
                        lat,
                        lng,
                        radius,
                        categoryCode
                );

        for (KakaoMapResponse.Document d : categoryResp.getDocuments()) {
            merged.putIfAbsent(d.getPlaceName(), d);
        }

        /* ============================
           2️⃣ (선택) 키워드 기반 확장
           ============================ */
        List<String> keywords =
                keywordWeightAggregator.getTopKeywordNames(user, KEYWORD_LIMIT);

        for (String keyword : keywords) {
            KakaoMapResponse keywordResp =
                    kakaoMapClient.searchByKeywordWithLocation(
                            keyword,
                            lat,
                            lng,
                            radius,
                            1,
                            PAGE_SIZE
                    );

            for (KakaoMapResponse.Document d : keywordResp.getDocuments()) {
                merged.putIfAbsent(d.getPlaceName(), d);
            }
        }

        /* ============================
           3️⃣ 공통 응답 생성
           ============================ */
        KakaoMapResponse result = new KakaoMapResponse();
        result.applyDocuments(new ArrayList<>(merged.values()));
        return result;
    }
}
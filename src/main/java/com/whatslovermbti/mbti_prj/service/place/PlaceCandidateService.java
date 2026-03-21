package com.whatslovermbti.mbti_prj.service.place;

import com.whatslovermbti.mbti_prj.constant.Category;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoCategoryMapper;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoCourseCategory;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapClient;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceCandidateService {
    private final KakaoMapClient kakaoMapClient;
    private static final int TARGET_SIZE = 45;

    @Cacheable(
            value = "kakaoCandidateCache",
            key = "'candidate:' + #lat + ':' + #lng + ':' + #radius + ':' + #category",
            unless = "#result == null || #result.isEmpty()"
    )
    @Transactional
    public List<KakaoMapResponse.Document> fetchCandidates(
            double lat,
            double lng,
            int radius,
            Category category
    ) {
        log.info("Kakao API CALL (CANDIDATE - CATEGORY PAGINATION)");

        if (category == Category.COURSE) {
            return searchCourseCandidates(lat, lng, radius);
        }

        String categoryCode = KakaoCategoryMapper.toKakaoCode(category);
        return searchSingleCategory(lat, lng, radius, categoryCode);
    }

    private List<KakaoMapResponse.Document> searchSingleCategory(
            double lat,
            double lng,
            int radius,
            String categoryCode
    ) {
        Map<String, KakaoMapResponse.Document> merged = new LinkedHashMap<>();

        int page = 1;
        boolean isEnd = false;
        int MAX_PAGE = 5;

        while (!isEnd && page <= MAX_PAGE) {

            KakaoMapResponse resp =
                    kakaoMapClient.searchByCategory(
                            lat, lng, radius, page, categoryCode
                    );

            if (resp == null || resp.getDocuments() == null) {
                break;
            }

            for (KakaoMapResponse.Document d : resp.getDocuments()) {
                merged.putIfAbsent(d.getId(), d);
            }

            isEnd = resp.getMeta() != null && resp.getMeta().isEnd();
            page++;
        }

        return new ArrayList<>(merged.values());
    }


    private List<KakaoMapResponse.Document> searchCourseCandidates(
            double lat,
            double lng,
            int radius
    ) {
        Map<String, KakaoMapResponse.Document> merged = new LinkedHashMap<>();

        for (String code : KakaoCourseCategory.codes()) {

            int page = 1;
            boolean isEnd = false;
            int MAX_PAGE = 3;

            while (!isEnd && page <= MAX_PAGE) {

                KakaoMapResponse resp =
                        kakaoMapClient.searchByCategory(
                                lat, lng, radius, page, code
                        );

                if (resp == null || resp.getDocuments() == null) {
                    break;
                }

                for (KakaoMapResponse.Document d : resp.getDocuments()) {
                    if (isExcludedCourseFacility(d)) {
                        continue;
                    }

                    merged.putIfAbsent(d.getId(), d);
                    if (merged.size() >= TARGET_SIZE) {
                        return new ArrayList<>(merged.values());
                    }
                }

                isEnd = resp.getMeta() != null && resp.getMeta().isEnd();
                page++;
            }
        }

        return new ArrayList<>(merged.values());
    }

    private boolean isExcludedCourseFacility(KakaoMapResponse.Document d) {
        String category = d.getCategoryName();
        if (category == null) return false;

        return category.contains("주차장")
                || category.contains("화장실")
                || category.contains("매표")
                || category.contains("안내")
                || category.contains("숙박");
    }
}
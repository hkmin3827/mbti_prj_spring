package com.whatslovermbti.mbti_prj.service.place;
// TODO : JSON 파싱, Place 엔티티 변환, MBTI 가중치 적용

import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapClient;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
import com.whatslovermbti.mbti_prj.infra.kakao.dto.KakaoKeywordResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlaceSearchService {

    private final KakaoMapClient kakaoMapClient;

    @Cacheable(
            value = "kakaoPlaceCache",
            key = "'nearby:' + #lat + ':' + #lng + ':' + #radius + ':' + #categoryCode"
    )
    public KakaoMapResponse searchNearby(double lat, double lng, int radius, String categoryCode) {
        log.info("Kakao API CALL"); // 캐시 검증용
        return kakaoMapClient.searchNearbyByCategory(lat, lng, radius, categoryCode);
    }

    @Cacheable(
            value = "kakaoKeywordCache",
            key = "'keyword:' + #keyword"
    )
    public KakaoKeywordResponse searchByKeyword(
            String keyword,
            int page,
            int size
    ) {
        log.info("Kakao API CALL"); // 캐시 검증용
        return kakaoMapClient.searchByKeyword(keyword, page, size);
    }
}
package com.whatslovermbti.mbti_prj.service.place;

import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapClient;
import com.whatslovermbti.mbti_prj.repository.PlaceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceSyncService {

    private final PlaceRepository placeRepository;
    private final KakaoMapClient kakaoMapClient;

    /**
     * 카카오 기준 place 유효성 검사
     */
    public void validatePlace(Place place) {

        boolean existsOnKakao = kakaoMapClient.existsPlace(place.getKakaoPlaceId());

        if (!existsOnKakao && !place.isDeleted()) {
            place.softDelete();
        }

        if (existsOnKakao && place.isDeleted()) {
            place.restore(); // 다시 살아난 경우
        }
    }
}
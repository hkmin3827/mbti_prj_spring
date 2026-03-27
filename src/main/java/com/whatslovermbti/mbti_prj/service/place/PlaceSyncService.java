package com.whatslovermbti.mbti_prj.service.place;

import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceSyncService {

    private final KakaoMapClient kakaoMapClient;

    public void validatePlace(Place place) {

        boolean existsOnKakao = kakaoMapClient.existsPlace(place.getKakaoPlaceId());

        if (!existsOnKakao && !place.isDeleted()) {
            place.softDelete();
        }

        if (existsOnKakao && place.isDeleted()) {
            place.restore();
        }
    }
}
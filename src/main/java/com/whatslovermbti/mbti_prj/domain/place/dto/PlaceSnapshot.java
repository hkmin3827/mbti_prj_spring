package com.whatslovermbti.mbti_prj.domain.place.dto;

import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;

public record PlaceSnapshot(
        String kakaoPlaceId,
        String name,
        String categoryName,
        String categoryGroupCode,
        String address,
        String roadAddress,
        Double latitude,
        Double longitude,
        String phone,
        String placeUrl
) {
    public static PlaceSnapshot from(KakaoMapResponse.Document doc) {
        return new PlaceSnapshot(
                doc.getId(),
                doc.getPlaceName(),
                doc.getCategoryName(),
                doc.getCategoryGroupCode(),
                doc.getAddressName(),
                doc.getRoadAddressName(),
                Double.parseDouble(doc.getLatitude()),
                Double.parseDouble(doc.getLongitude()),
                doc.getPhone(),
                doc.getPlaceUrl()
        );
    }
}
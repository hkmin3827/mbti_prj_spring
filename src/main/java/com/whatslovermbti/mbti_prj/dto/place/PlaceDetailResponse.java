package com.whatslovermbti.mbti_prj.dto.place;

import com.whatslovermbti.mbti_prj.constant.ActionType;
import com.whatslovermbti.mbti_prj.constant.Category;
import com.whatslovermbti.mbti_prj.entity.Place;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceDetailResponse {

    // Place 기본 정보
    private Long id;
    private String name;
    private Category category;

    private String address;
    private String roadAddress;
    private Double latitude;
    private Double longitude;

    private Double rating;        // 리뷰 평균 평점
    private String description;   // 장소 설명

    private String kakaoPlaceId;
    private String telnum;

    // User 기준 정보
    private ActionType myReaction;   // LIKE / DISLIKE / null
    private boolean bookmarked;

    /**
     * Place 엔티티 기반 기본 정보 세팅용
     * (user 상태는 QueryService에서 추가)
     */
    public static PlaceDetailResponse.PlaceDetailResponseBuilder fromPlace(
            Place place
    ) {
        return PlaceDetailResponse.builder()
                .id(place.getId())
                .name(place.getName())
                .category(place.getCategory())
                .address(place.getAddress())
                .roadAddress(place.getRoadAddress())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .rating(place.getRating())
                .description(place.getDescription())
                .kakaoPlaceId(place.getKakaoPlaceId())
                .telnum(place.getTelnum());
    }
}

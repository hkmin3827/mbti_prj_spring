package com.whatslovermbti.mbti_prj.dto.place;

import com.whatslovermbti.mbti_prj.constant.ActionType;
import com.whatslovermbti.mbti_prj.constant.Category;
import com.whatslovermbti.mbti_prj.entity.Place;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceDetailResponse {

    private Long id;
    private String name;
    private Category category;

    private String address;
    private String roadAddress;
    private Double latitude;
    private Double longitude;

    private Double rating;
    private String description;

    private String kakaoPlaceId;
    private String telnum;

    private ActionType myReaction;
    private boolean bookmarked;

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

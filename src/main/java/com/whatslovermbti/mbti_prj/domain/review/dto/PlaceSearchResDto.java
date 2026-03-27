package com.whatslovermbti.mbti_prj.domain.review.dto;

import com.whatslovermbti.mbti_prj.domain.place.entity.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaceSearchResDto {

    private Long id;
    private String name;
    private String address;
    private String roadAddress;

    public static PlaceSearchResDto from(Place place) {
        return new PlaceSearchResDto(
                place.getId(),
                place.getName(),
                place.getAddress(),
                place.getRoadAddress()
        );
    }
}

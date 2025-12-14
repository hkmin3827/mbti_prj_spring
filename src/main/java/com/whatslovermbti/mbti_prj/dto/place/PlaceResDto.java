package com.whatslovermbti.mbti_prj.dto.place;

import com.whatslovermbti.mbti_prj.constant.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PlaceResDto {
    private Long id;
    private String name;
    private Category category;

    private String address;
    private Double latitude;
    private Double longitude;

    private Double rating;
    private List<String> imageUrls;

    private String mapPlaceId;

    // 추천/표시용
    private List<String> keywords;
    private Double distance;   // km (계산값)
}
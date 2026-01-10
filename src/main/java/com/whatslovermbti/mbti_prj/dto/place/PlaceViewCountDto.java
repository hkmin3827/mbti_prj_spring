package com.whatslovermbti.mbti_prj.dto.place;

import com.whatslovermbti.mbti_prj.constant.Category;

public record PlaceViewCountDto(
        Long placeId,
        String name,
        String address,
        String roadAddress,
        Category category,
        Double rating,
        Long viewCount
) {}
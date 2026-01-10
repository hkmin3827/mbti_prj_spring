package com.whatslovermbti.mbti_prj.dto.place;

import com.whatslovermbti.mbti_prj.constant.Category;

public record PlaceReviewCountDto(
        Long placeId,
        String name,
        Category category,
        String address,
        String roadAddress,
        Double rating,
        String kakaoPlaceId,
        String telnum,
        long reviewCount
) {}
package com.whatslovermbti.mbti_prj.domain.review.dto;

import com.whatslovermbti.mbti_prj.global.constant.Category;

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
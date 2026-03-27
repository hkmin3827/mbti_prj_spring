package com.whatslovermbti.mbti_prj.domain.action.view.dto;

import com.whatslovermbti.mbti_prj.global.constant.Category;

public record PlaceViewCountDto(
        Long placeId,
        String name,
        String address,
        String roadAddress,
        Category category,
        Double rating,
        Long viewCount
) {}
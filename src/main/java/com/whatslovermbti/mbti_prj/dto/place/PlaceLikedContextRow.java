package com.whatslovermbti.mbti_prj.dto.place;

import com.whatslovermbti.mbti_prj.constant.MbtiContext;

import java.time.LocalDateTime;

public record PlaceLikedContextRow(
        Long placeId,
        MbtiContext context,
        LocalDateTime latestAt
) {}
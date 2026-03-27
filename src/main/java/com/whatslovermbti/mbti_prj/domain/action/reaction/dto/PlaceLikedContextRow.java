package com.whatslovermbti.mbti_prj.domain.action.reaction.dto;

import com.whatslovermbti.mbti_prj.global.constant.MbtiContext;

import java.time.LocalDateTime;

public record PlaceLikedContextRow(
        Long placeId,
        MbtiContext context,
        LocalDateTime latestAt
) {}
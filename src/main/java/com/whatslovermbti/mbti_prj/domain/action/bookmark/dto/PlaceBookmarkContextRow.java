package com.whatslovermbti.mbti_prj.domain.action.bookmark.dto;

import com.whatslovermbti.mbti_prj.global.constant.MbtiContext;

public record PlaceBookmarkContextRow(
        Long placeId,
        MbtiContext context
) {}
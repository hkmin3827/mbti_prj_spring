package com.whatslovermbti.mbti_prj.dto.place;

import com.whatslovermbti.mbti_prj.constant.MbtiContext;

public record PlaceBookmarkContextRow(
        Long placeId,
        MbtiContext context
) {}
package com.whatslovermbti.mbti_prj.domain.review.dto;

import lombok.Getter;

@Getter
public class ReviewUpdateReqDto {
    private int rating;
    private String content;
    private String reviewImageUrl;
    private boolean removeImage;
}

package com.whatslovermbti.mbti_prj.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewResDto {
    private Long id;
    private String userName;
    private int rating;
    private String content;
    private boolean verified;
    private LocalDateTime createdAt;
    private String reviewImageUrl;
}
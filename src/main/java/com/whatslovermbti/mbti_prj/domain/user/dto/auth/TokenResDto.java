package com.whatslovermbti.mbti_prj.domain.user.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResDto {
    private String token;
    private boolean profileCompleted;
}
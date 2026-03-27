package com.whatslovermbti.mbti_prj.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MbtiProfileReq {
    @NotBlank(message = "회원 MBTI 입력은 필수입니다.")
    private String mbti;
    private String partnerMbti;
}

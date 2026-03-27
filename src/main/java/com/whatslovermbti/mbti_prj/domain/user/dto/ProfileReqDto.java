package com.whatslovermbti.mbti_prj.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileReqDto {
    private String name;
    private String profileImage;

    @NotBlank(message = "회원 MBTI 입력은 필수입니다.")
    private String mbti;
    private String partnerMbti;

    @NotBlank(message = "회원 전화번호 입력은 필수입니다.")
    @Pattern(regexp = "^010\\d{8}$", message = "전화번호는 010으로 시작하고 11자리여야 합니다.")
    private String telnum;
}
package com.whatslovermbti.mbti_prj.dto.auth;

import com.whatslovermbti.mbti_prj.constant.Provider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class SignUpReqDto {
    private String email;
    private String password;
    private String name;
    private String mbti;
    private String partnerMbti;
    private Provider provider;

    private String telnum;
}

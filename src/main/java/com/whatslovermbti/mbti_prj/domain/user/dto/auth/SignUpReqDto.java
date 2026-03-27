package com.whatslovermbti.mbti_prj.domain.user.dto.auth;

import com.whatslovermbti.mbti_prj.global.constant.Provider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class SignUpReqDto {
    private String email;
    private String password;
    private String name;
    private Provider provider;

    private String telnum;
}

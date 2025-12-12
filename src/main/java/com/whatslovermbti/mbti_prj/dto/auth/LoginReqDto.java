package com.whatslovermbti.mbti_prj.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginReqDto {
    private String email;
    private String password;
}

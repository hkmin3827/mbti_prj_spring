package com.whatslovermbti.mbti_prj.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 등록된 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증된 사용자가 아닙니다.");  // 401 UNAUTHORIZED

    private final HttpStatus status;
    private final String message;
}

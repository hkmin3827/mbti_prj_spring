package com.whatslovermbti.mbti_prj.global.exception;

import com.whatslovermbti.mbti_prj.global.constant.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
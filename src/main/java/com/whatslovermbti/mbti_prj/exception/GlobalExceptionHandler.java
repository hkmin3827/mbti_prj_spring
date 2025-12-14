package com.whatslovermbti.mbti_prj.exception;

import com.whatslovermbti.mbti_prj.constant.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // (전역 예외 처리)
public class GlobalExceptionHandler {

    // 공통 에러 응답 객체
    static class ErrorResponse {
        private final int status;
        private final String code;
        private final String message;

        public ErrorResponse(ErrorCode errorCode) {
            this.status = errorCode.getStatus().value();
            this.code = errorCode.name();
            this.message = errorCode.getMessage();
        }

        public int getStatus() { return status; }
        public String getCode() { return code; }
        public String getMessage() { return message; }
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {

        ErrorCode code = ex.getErrorCode();

        ErrorResponse response = new ErrorResponse(code);

        return new ResponseEntity<>(response, code.getStatus());
    }
}
package com.whatslovermbti.mbti_prj.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "이미 등록된 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증된 사용자가 아닙니다."),  // 401 UNAUTHORIZED
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "장소를 찾을 수 없습니다."),
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "북마크를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),
    REACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요한 내역을 찾을 수 없습니다."),
    PARTNER_MBTI_NOT_SET(HttpStatus.BAD_REQUEST, "파트너 MBTI가 설정되지 않았습니다."),
    INVALID_TARGET_MBTI(HttpStatus.BAD_REQUEST, "지원하지 않는 MBTI 타입입니다."),
    INVALID_MBTI_TYPE(HttpStatus.BAD_REQUEST, "존재하지 않는 MBTI 유형입니다."),
    PASSWORD_REQUIRED(HttpStatus.BAD_REQUEST, "비밀번호가 필요합니다"),
    PROFILE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 프로필이 등록된 사용자 입니다."),
    PROFILE_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "프로필 필수 값이 입력되지 않았습니다."),
    USER_ALREADY_ACTIVE(HttpStatus.CONFLICT, "이미 활성화된 회원 입니다."),
    USER_ALREADY_INACTIVE(HttpStatus.CONFLICT, "이미 비활성화된 회원 입니다."),
    DUPLICATE_RECEIPT_REVIEW(HttpStatus.CONFLICT, "이미 리뷰 작성한 영수증 사진 입니다."),
    PLACE_DELETED(HttpStatus.NOT_FOUND, "해당 장소 정보는 더 이상 제공되지 않습니다."),
    INVALID_UPLOAD_FOLDER(HttpStatus.BAD_REQUEST,"허용되지 않은 업로드 경로입니다."),
    INVALID_FILE_NAME(HttpStatus.BAD_REQUEST, "잘못된 파일명입니다."),
    INVALID_CONTENT_TYPE(HttpStatus.BAD_REQUEST, "잘못된 파일형식입니다.");

    private final HttpStatus status;
    private final String message;
}

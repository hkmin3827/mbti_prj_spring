package com.whatslovermbti.mbti_prj.global.util;

import com.whatslovermbti.mbti_prj.global.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.global.constant.MbtiAxis;
import com.whatslovermbti.mbti_prj.global.exception.CustomException;

import java.util.EnumSet;

public final class MbtiValidator {

    private MbtiValidator() {}

    public static void validate(String mbti) {
        if (mbti == null || mbti.length() != 4) {
            throw new CustomException(ErrorCode.INVALID_MBTI_TYPE);
        }

        validateAxis(mbti.charAt(0), EnumSet.of(MbtiAxis.I, MbtiAxis.E));
        validateAxis(mbti.charAt(1), EnumSet.of(MbtiAxis.N, MbtiAxis.S));
        validateAxis(mbti.charAt(2), EnumSet.of(MbtiAxis.F, MbtiAxis.T));
        validateAxis(mbti.charAt(3), EnumSet.of(MbtiAxis.J, MbtiAxis.P));
    }

    private static void validateAxis(char value, EnumSet<MbtiAxis> allowed) {
        try {
            MbtiAxis axis = MbtiAxis.valueOf(String.valueOf(value));
            if (!allowed.contains(axis)) {
                throw new CustomException(ErrorCode.INVALID_MBTI_TYPE);
            }
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_MBTI_TYPE);
        }
    }
}
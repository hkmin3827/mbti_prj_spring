package com.whatslovermbti.mbti_prj.global.guard;

import com.whatslovermbti.mbti_prj.global.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.global.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.domain.user.entity.User;
import com.whatslovermbti.mbti_prj.global.exception.CustomException;
import org.springframework.stereotype.Component;

@Component
public class MbtiContextGuard {

    public void validate(
            MbtiContext context,
            User user
    ) {
        if (context == MbtiContext.PARTNER &&
                user.getPartnerMbti() == null) {
            throw new CustomException(ErrorCode.PARTNER_MBTI_NOT_SET);
        }
    }
}
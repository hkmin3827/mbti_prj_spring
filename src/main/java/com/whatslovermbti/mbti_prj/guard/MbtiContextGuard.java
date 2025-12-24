package com.whatslovermbti.mbti_prj.guard;

import com.whatslovermbti.mbti_prj.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.exception.CustomException;
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
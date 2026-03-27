package com.whatslovermbti.mbti_prj.global.resolver.context;

import com.whatslovermbti.mbti_prj.global.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.global.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.domain.user.entity.User;
import com.whatslovermbti.mbti_prj.global.exception.CustomException;
import org.springframework.stereotype.Component;

@Component
public class TargetMbtiResolver {
    public String resolve(User user, MbtiContext context) {

        if (context == MbtiContext.SELF) {
            return user.getMbti();
        }

        if (context == MbtiContext.PARTNER) {
            if (user.getPartnerMbti() == null) {
                throw new CustomException(ErrorCode.PARTNER_MBTI_NOT_SET);
            }
            return user.getPartnerMbti();
        }

        throw new CustomException(ErrorCode.INVALID_TARGET_MBTI);
    }
}
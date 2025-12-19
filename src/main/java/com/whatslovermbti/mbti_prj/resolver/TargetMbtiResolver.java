package com.whatslovermbti.mbti_prj.resolver;

import com.whatslovermbti.mbti_prj.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.exception.CustomException;
import org.springframework.stereotype.Component;

@Component
public class TargetMbtiResolver {

    public String resolve(User user, MbtiContext targetMbti) {

        if (targetMbti == MbtiContext.SELF) {
            return user.getMbti();
        }

        if (targetMbti == MbtiContext.PARTNER) {
            if (user.getPartnerMbti() == null) {
                throw new CustomException(ErrorCode.PARTNER_MBTI_NOT_SET);
            }
            return user.getPartnerMbti();
        }

        throw new CustomException(ErrorCode.INVALID_TARGET_MBTI);
    }
}
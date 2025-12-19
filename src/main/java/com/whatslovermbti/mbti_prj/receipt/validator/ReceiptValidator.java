package com.whatslovermbti.mbti_prj.receipt.validator;

import com.whatslovermbti.mbti_prj.receipt.model.ReceiptInfo;
import org.springframework.stereotype.Component;

@Component
public class ReceiptValidator {

    public void validate(ReceiptInfo info) {
        if (info.getStoreName() == null)
            throw new IllegalStateException("상호명 없음");

        if (info.getTotalAmount() == null)
            throw new IllegalStateException("금액 없음");

        if (info.getDate() == null)
            throw new IllegalStateException("날짜 없음");
    }
}

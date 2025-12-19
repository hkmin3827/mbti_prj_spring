package com.whatslovermbti.mbti_prj.service.ocr;

import com.whatslovermbti.mbti_prj.receipt.model.ReceiptInfo;
import org.springframework.stereotype.Service;


@Service
public class ReceiptScoringService {
    public int score(ReceiptInfo info) {
        int score = 0;

        if (info.getStoreName() != null) score += 30;
        if (info.getAddress() != null) score += 20;
        if (info.getTotalAmount() != null) score += 30;
        if (info.getDate() != null) score += 20;

        return score;
    }
}

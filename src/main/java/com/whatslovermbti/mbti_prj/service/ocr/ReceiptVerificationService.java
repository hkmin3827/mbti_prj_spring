package com.whatslovermbti.mbti_prj.service.ocr;

import com.whatslovermbti.mbti_prj.receipt.model.ReceiptInfo;
import com.whatslovermbti.mbti_prj.receipt.model.ScoreVO;
import com.whatslovermbti.mbti_prj.receipt.parser.ReceiptParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptVerificationService {

    private final ReceiptParser receiptParser;
    private final ReceiptScoringService receiptScoringService;

    public ScoreVO verify(List<String> lines) {
        ReceiptInfo receipt = receiptParser.parse(lines);
        int score = receiptScoringService.score(receipt);
        return new ScoreVO(receipt, score);
    }
}
package com.whatslovermbti.mbti_prj.receipt.model;

import lombok.Getter;

@Getter
public class ScoreVO {
    private final ReceiptInfo receipt;
    private final int score;
    private final boolean verified;

    public ScoreVO(ReceiptInfo receipt, int score) {
        this.receipt = receipt;
        this.score = score;
        this.verified = score >= 80;
    }
}

package com.whatslovermbti.mbti_prj.infra.receipt.model;

import lombok.Getter;

import java.util.List;

@Getter
public class ReceiptParseContext {
    private final List<String> lines;
    private final ReceiptInfo receipt = new ReceiptInfo();

    public ReceiptParseContext(List<String> lines) {
        this.lines = lines;
    }
}

package com.whatslovermbti.mbti_prj.infra.receipt.parser;

import com.whatslovermbti.mbti_prj.infra.receipt.model.ReceiptInfo;

import java.util.List;

public interface ReceiptParser {
    ReceiptInfo parse(List<String> lines);
}
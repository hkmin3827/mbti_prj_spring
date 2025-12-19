package com.whatslovermbti.mbti_prj.receipt.parser;

import com.whatslovermbti.mbti_prj.receipt.model.ReceiptInfo;

import java.util.List;

public interface ReceiptParser {
    ReceiptInfo parse(List<String> lines);
}
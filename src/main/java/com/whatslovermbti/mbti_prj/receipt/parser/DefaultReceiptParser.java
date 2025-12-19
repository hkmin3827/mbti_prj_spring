package com.whatslovermbti.mbti_prj.receipt.parser;

import com.whatslovermbti.mbti_prj.receipt.extractor.AddressExtractor;
import com.whatslovermbti.mbti_prj.receipt.extractor.AmountExtractor;
import com.whatslovermbti.mbti_prj.receipt.extractor.DateExtractor;
import com.whatslovermbti.mbti_prj.receipt.extractor.StoreNameExtractor;
import com.whatslovermbti.mbti_prj.receipt.model.ReceiptParseContext;
import com.whatslovermbti.mbti_prj.receipt.model.ReceiptInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultReceiptParser implements ReceiptParser {

    private final StoreNameExtractor storeNameExtractor;
    private final AddressExtractor addressExtractor;
    private final AmountExtractor amountExtractor;
    private final DateExtractor dateExtractor;

    @Override
    public ReceiptInfo parse(List<String> lines) {
        ReceiptParseContext ctx = new ReceiptParseContext(lines);

        storeNameExtractor.extract(ctx);
        addressExtractor.extract(ctx);
        amountExtractor.extract(ctx);
        dateExtractor.extract(ctx);

        return ctx.getReceipt();
    }
}
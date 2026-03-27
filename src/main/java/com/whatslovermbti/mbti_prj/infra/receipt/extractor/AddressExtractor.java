package com.whatslovermbti.mbti_prj.infra.receipt.extractor;

import com.whatslovermbti.mbti_prj.infra.receipt.model.ReceiptParseContext;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class AddressExtractor {

    private static final Pattern ADDRESS_PATTERN =
            Pattern.compile(".*(로|길)\\s?\\d+.*|.*\\d+(-\\d+)?번지.*");

    public void extract(ReceiptParseContext ctx) {
        for (String line : ctx.getLines()) {

            String normalized = line.replace("주소", "")
                    .replace(":", "")
                    .trim();

            if (ADDRESS_PATTERN.matcher(normalized).matches()) {
                ctx.getReceipt().setAddress(normalized);
                return;
            }
        }
    }
}
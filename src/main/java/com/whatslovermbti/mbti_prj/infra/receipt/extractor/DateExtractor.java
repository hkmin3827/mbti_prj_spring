package com.whatslovermbti.mbti_prj.infra.receipt.extractor;

import com.whatslovermbti.mbti_prj.infra.receipt.model.ReceiptParseContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DateExtractor {

    private static final Pattern DATE_PATTERN =
            Pattern.compile("\\d{4}[-./]\\d{2}[-./]\\d{2}");

    public void extract(ReceiptParseContext ctx) {
        for (String line : ctx.getLines()) {
            Matcher m = DATE_PATTERN.matcher(line);
            if (m.find()) {
                ctx.getReceipt().setDate(
                        LocalDate.parse(m.group().replace(".", "-"))
                );
                return;
            }
        }
    }
}
package com.whatslovermbti.mbti_prj.receipt.extractor;

import com.whatslovermbti.mbti_prj.receipt.model.ReceiptParseContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoreNameExtractor {

    private static final List<String> STORE_NAME_BLACKLIST = List.of(
            "주문", "대기", "번호", "POS", "BILL", "결제", "카드",
            "합계", "총액", "부가세"
    );

    public void extract(ReceiptParseContext ctx) {

        for (String line : ctx.getLines()) {
            if (line.contains("상호")) {
                String name = line.replaceAll("상호[:：]?", "").trim();
                if (isValidStoreName(name)) {
                    ctx.getReceipt().setStoreName(name);
                    return;
                }
            }
        }

        for (String line : ctx.getLines()) {
            if (!line.matches(".*\\d+.*")
                    && line.length() > 3
                    && isValidStoreName(line)) {
                ctx.getReceipt().setStoreName(line);
                return;
            }
        }
    }

    private boolean isValidStoreName(String line) {
        return STORE_NAME_BLACKLIST.stream().noneMatch(line::contains);
    }
}

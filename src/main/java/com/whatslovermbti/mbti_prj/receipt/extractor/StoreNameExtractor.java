package com.whatslovermbti.mbti_prj.receipt.extractor;

import com.whatslovermbti.mbti_prj.receipt.model.ReceiptParseContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoreNameExtractor {

    // 상호명에서 제외해야 할 키워드
    private static final List<String> STORE_NAME_BLACKLIST = List.of(
            "주문", "대기", "번호", "POS", "BILL", "결제", "카드",
            "합계", "총액", "부가세"
    );

    // 실무에서는 브랜드 사전을 둔다 (지금은 최소셋)
//    private static final List<String> BRAND_KEYWORDS = List.of(
//            "메가", "MGC", "커피", "카페", "스타벅스", "이디야", "투썸"
//    );

    public void extract(ReceiptParseContext ctx) {

        // "상호:" 명시된 경우 (최우선)
        for (String line : ctx.getLines()) {
            if (line.contains("상호")) {
                String name = line.replaceAll("상호[:：]?", "").trim();
                if (isValidStoreName(name)) {
                    ctx.getReceipt().setStoreName(name);
                    return;
                }
            }
        }

        // 브랜드 키워드 기반
//        for (String line : ctx.getLines()) {
//            if (BRAND_KEYWORDS.stream().anyMatch(line::contains)
//                    && isValidStoreName(line)) {
//                ctx.getReceipt().setStoreName(line);
//                return;
//            }
//        }

        // fallback (숫자 없는 라인 + 블랙리스트 제외)
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

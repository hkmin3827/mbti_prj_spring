package com.whatslovermbti.mbti_prj.infra.receipt.extractor;

import com.whatslovermbti.mbti_prj.infra.receipt.model.ReceiptParseContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AmountExtractor {

    private static final List<String> AMOUNT_KEYWORDS = List.of(
            "합계", "합 계", "총액", "결제", "결제금액", "카드결제", "받은금액",
            "소계", "소 계", "매출합계", "매출 합계"
    );

    private static final List<String> AMOUNT_BLACKLIST = List.of(
            "카드번호", "승인번호", "****", "POS", "BILL", "영수증번호", "사업자번호"
    );

    private static final int MAX_REASONABLE_AMOUNT = 5_000_000;
    private static final int MIN_REASONABLE_AMOUNT = 100;

    private static final Pattern MONEY_PATTERN =
            Pattern.compile("(\\d{1,3}([,]\\d{3})+|\\d+)([.]\\d{3})?");

    public void extract(ReceiptParseContext ctx) {
        List<String> lines = ctx.getLines();
        List<Integer> candidates = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (isBlacklisted(line)) continue;

            boolean hasKeyword = AMOUNT_KEYWORDS.stream().anyMatch(k -> normalize(line).contains(k.replace(" ", "")));
            if (!hasKeyword) continue;

            Integer v = parseMoneyFromLine(line);
            if (v != null) {
                addIfValid(candidates, v);
                continue;
            }

            for (int j = 1; j <= 2 && i + j < lines.size(); j++) {
                String next = lines.get(i + j);
                if (isBlacklisted(next)) break;

                Integer nv = parseMoneyFromLine(next);
                if (nv != null) {
                    addIfValid(candidates, nv);
                    break;
                }
            }
        }

        candidates.stream()
                .max(Integer::compareTo)
                .ifPresent(ctx.getReceipt()::setTotalAmount);
    }

    private void addIfValid(List<Integer> candidates, int value) {
        if (value < MIN_REASONABLE_AMOUNT) return;
        if (value > MAX_REASONABLE_AMOUNT) return;
        candidates.add(value);
    }

    private boolean isBlacklisted(String line) {
        String n = normalize(line);
        return AMOUNT_BLACKLIST.stream().anyMatch(b -> n.contains(b.replace(" ", "")));
    }

    private String normalize(String s) {
        return s == null ? "" : s.replaceAll("\\s+", "").trim();
    }

    private Integer parseMoneyFromLine(String line) {
        if (line == null) return null;

        String cleaned = line.replaceAll("^\\s*\\d+\\s*[.)]\\s*", "");

        Matcher m = MONEY_PATTERN.matcher(cleaned);
        Integer best = null;

        while (m.find()) {
            String token = m.group();

            token = token.replace(".", ",");

            int value = toInt(token);
            if (best == null || value > best) best = value;
        }

        return best;
    }

    private int toInt(String token) {
        String num = token.replaceAll("[^0-9]", "");
        if (num.isBlank()) return -1;
        return Integer.parseInt(num);
    }
}
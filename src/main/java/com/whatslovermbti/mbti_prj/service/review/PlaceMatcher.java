package com.whatslovermbti.mbti_prj.service.review;

import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.receipt.model.ReceiptInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlaceMatcher {

    private static final double NAME_MATCH_THRESHOLD = 0.4; // 40% 이상 공통
    private static final double ADDRESS_MATCH_THRESHOLD = 0.5;

    public int match(Place place, ReceiptInfo receipt) {


        log.info("========== PLACE MATCH START ==========");
        log.info("[PLACE] name={}, address={}, roadAddress={}",
                place.getName(),
                place.getAddress(),
                place.getRoadAddress()
        );

        log.info("[RECEIPT] storeName={}, address={}, roadAddress={}",
                receipt.getStoreName(),
                receipt.getAddress()
        );
        int score = 0;
        boolean nameMatched = matchByTokenSimilarity(
                place.getName(),
                receipt.getStoreName(),
                NAME_MATCH_THRESHOLD
        );
        if(nameMatched) score += 50;

        boolean addressMatched = matchByTokenSimilarity(
                place.getAddress(),
                receipt.getAddress(),
                ADDRESS_MATCH_THRESHOLD
        );

        boolean roadAddressMatched = matchByTokenSimilarity(
                place.getRoadAddress(),
                receipt.getAddress(),
                ADDRESS_MATCH_THRESHOLD
        );

        if (addressMatched || roadAddressMatched) {
            score += 50;
            log.info("[MATCH] ADDRESS matched (lot={}, road={}) (+50)",
                    addressMatched, roadAddressMatched);
        } else {
            log.info("[MATCH] ADDRESS not matched");
        }

        log.info("[RESULT] totalScore={}", score);
        return score;
    }

    /**
     * 토큰 기반 유사도 비교
     */
    private boolean matchByTokenSimilarity(
            String a,
            String b,
            double threshold
    ) {
        if (a == null || b == null) return false;

        Set<String> tokensA = tokenize(a);
        Set<String> tokensB = tokenize(b);

        if (tokensA.isEmpty() || tokensB.isEmpty()) return false;

        Set<String> intersection = new HashSet<>(tokensA);
        intersection.retainAll(tokensB);

        double similarity =
                (double) intersection.size()
                        / Math.min(tokensA.size(), tokensB.size());

        return similarity >= threshold;
    }

    /**
     * OCR 대응 정규화 + 토큰화
     */
    private Set<String> tokenize(String s) {

        String normalized = s.toLowerCase()
                .replaceAll("[^a-z0-9가-힣 ]", " ")
                .replaceAll("\\d+", " ")
                .replaceAll("(점|지점|본점|층|호)", " ")
                .replaceAll("\\s+", " ")
                .trim();

        return Arrays.stream(normalized.split(" "))
                .filter(t -> t.length() >= 2) // 2글자 이상만
                .collect(Collectors.toSet());
    }
}

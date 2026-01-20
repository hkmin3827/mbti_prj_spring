package com.whatslovermbti.mbti_prj.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomPicker {

    private static final Random random = new Random();

    /**
     * 점수 내림차순 정렬된 리스트에서
     * high / middle / low 구간을 나누고
     * 각 구간 내부를 셔플한 뒤 비율대로 픽
     */
    public static <T> List<T> pickWithBias(
            List<T> sortedByScore,
            int topRange,
            int pickCount
    ) {
        if (sortedByScore == null || sortedByScore.isEmpty()) {
            return List.of();
        }

        int total = Math.min(sortedByScore.size(), topRange);

        int highCount   = (int) Math.round(pickCount * 0.4); // 40%
        int middleCount = (int) Math.round(pickCount * 0.4); // 40%
        int lowCount    = pickCount - highCount - middleCount;  // 20%

        int highEnd   = Math.min(total / 3, total);
        int middleEnd = Math.min(highEnd + total / 3, total);

        List<T> high =
                new ArrayList<>(sortedByScore.subList(0, highEnd));
        List<T> middle =
                new ArrayList<>(sortedByScore.subList(highEnd, middleEnd));
        List<T> low =
                new ArrayList<>(sortedByScore.subList(middleEnd, total));

        // 그룹 내부도 셔플
        Collections.shuffle(high, random);
        Collections.shuffle(middle, random);
        Collections.shuffle(low, random);

        List<T> result = new ArrayList<>(pickCount);

        result.addAll(high.stream()
                .limit(highCount)
                .toList());

        result.addAll(middle.stream()
                .limit(middleCount)
                .toList());

        result.addAll(low.stream()
                .limit(lowCount)
                .toList());

        Collections.shuffle(result, random);

        return result;
    }
}

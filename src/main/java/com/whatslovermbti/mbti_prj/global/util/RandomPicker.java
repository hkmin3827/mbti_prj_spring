package com.whatslovermbti.mbti_prj.global.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomPicker {

    private static final Random random = new Random();

    public static <T> List<T> pickWithBias(
            List<T> sortedByScore,
            int topRange,
            int pickCount
    ) {
        if (sortedByScore == null || sortedByScore.isEmpty()) {
            return List.of();
        }

        int total = Math.min(sortedByScore.size(), topRange);

        int highCount   = (int) Math.round(pickCount * 0.4);
        int middleCount = (int) Math.round(pickCount * 0.4);
        int lowCount    = pickCount - highCount - middleCount;

        int highEnd   = Math.min(total / 3, total);
        int middleEnd = Math.min(highEnd + total / 3, total);

        List<T> high =
                new ArrayList<>(sortedByScore.subList(0, highEnd));
        List<T> middle =
                new ArrayList<>(sortedByScore.subList(highEnd, middleEnd));
        List<T> low =
                new ArrayList<>(sortedByScore.subList(middleEnd, total));

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

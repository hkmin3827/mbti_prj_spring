package com.whatslovermbti.mbti_prj.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomPicker {

    private static final Random random = new Random();

    public static <T> List<T> shuffle(List<T> list) {
        Collections.shuffle(list, random);
        return list;
    }

    public static <T> List<T> pickTopWithRandom(
            List<T> source,
            int topN,
            int randomCount,
            int limit
    ) {
        if (source == null || source.isEmpty()) {
            return List.of();
        }

        // 🔥 반드시 mutable
        List<T> mutable = new ArrayList<>(source);

        int effectiveTopN = Math.min(topN, mutable.size());
        List<T> top = new ArrayList<>(mutable.subList(0, effectiveTopN));

        Collections.shuffle(top);

        return top.stream()
                .limit(limit)
                .toList(); // 결과는 불변이어도 OK
    }
}

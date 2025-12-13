package com.whatslovermbti.mbti_prj.util;

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
            List<T> list,
            int topN,
            int randomCount
    ) {
        if (list.size() <= topN) {
            return list;
        }

        List<T> top = list.subList(0, topN);
        List<T> rest = list.subList(topN, list.size());

        Collections.shuffle(rest);
        top.addAll(rest.subList(0, Math.min(randomCount, rest.size())));

        return top;
    }
}

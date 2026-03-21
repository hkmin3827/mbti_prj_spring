package com.whatslovermbti.mbti_prj.util;

import java.util.HashMap;
import java.util.Map;

public class MbtiAxisUtil {

    public static Map<String, Integer> parseAxes(String mbti) {
        Map<String, Integer> axes = new HashMap<>();

        if (mbti == null || mbti.length() != 4) {
            return axes;
        }

        axes.put(String.valueOf(mbti.charAt(0)).toUpperCase(), 1);
        axes.put(String.valueOf(mbti.charAt(1)).toUpperCase(), 1);
        axes.put(String.valueOf(mbti.charAt(2)).toUpperCase(), 1);
        axes.put(String.valueOf(mbti.charAt(3)).toUpperCase(), 1);

        return axes;
    }

    private MbtiAxisUtil() {}
}
package com.whatslovermbti.mbti_prj.constant;

import java.util.Set;

public enum KakaoCategoryCode {

    FD6(Category.FOOD),
    CE7(Category.CAFE),
    AT4(Category.COURSE),
    CT1(Category.COURSE),
    LEI(Category.COURSE);

    private final Category category;

    KakaoCategoryCode(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public static KakaoCategoryCode from(String code) {
        if (code == null) return null;
        for (KakaoCategoryCode c : values()) {
            if (code.startsWith(c.name())) {
                return c;
            }
        }
        return null;
    }

    public static Set<String> codes() {
        return Set.of("FD6", "CE7", "AT4", "CT1", "LEI");
    }
}

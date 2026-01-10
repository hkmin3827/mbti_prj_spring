package com.whatslovermbti.mbti_prj.infra.kakao;

import com.whatslovermbti.mbti_prj.constant.Category;
import com.whatslovermbti.mbti_prj.constant.KakaoCategoryCode;

public class KakaoCategoryMapper {

    public static String toKakaoCode(Category category) {
        return switch (category) {
            case FOOD -> "FD6";
            case CAFE -> "CE7";
            case COURSE  -> throw new IllegalStateException(
                    "COURSE 다중 호출 전용 메서드를 사용해야 함"
            );
        };
    }

    public static Category resolveCategory(String kakaoCategoryCode) {
        if (kakaoCategoryCode == null) {
            return Category.COURSE;
        }
        return switch (kakaoCategoryCode) {
            case "FD6" -> Category.FOOD;
            case "CE7" -> Category.CAFE;
            default -> Category.COURSE; // AT4, CT1, LEI 등
        };
    }

    public static Category resolveFromCategoryName(String categoryName) {
        if (categoryName == null) return Category.COURSE;

        if (categoryName.contains("카페")) return Category.CAFE;
        if (categoryName.contains("커피")) return Category.CAFE;
        if (categoryName.contains("음식")) return Category.FOOD;

        return Category.COURSE;
    }
}
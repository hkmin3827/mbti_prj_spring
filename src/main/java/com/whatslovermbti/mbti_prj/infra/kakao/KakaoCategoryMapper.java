package com.whatslovermbti.mbti_prj.infra.kakao;

import com.whatslovermbti.mbti_prj.constant.Category;

public class KakaoCategoryMapper {

    public static String toKakaoCode(Category category) {
        return switch (category) {
            case FOOD -> "FD6";
            case CAFE -> "CE7";
            case COURSE -> "AT4";
        };
    }
    /**
     * 카카오 category_name (예: "음식점 > 한식 > 백반,가정식")
     * → 우리 서비스 Category로 변환 (결과 분류용)
     */
    public static Category fromCategoryName(String categoryName) {

        if (categoryName == null || categoryName.isBlank()) {
            // 안전 장치: 알 수 없으면 COURSE(데이트 장소)로 분류
            return Category.COURSE;
        }

        // 대분류 기준 판단
        if (categoryName.startsWith("음식점")) {
            return Category.FOOD;
        }

        if (categoryName.startsWith("카페")) {
            return Category.CAFE;
        }

        if (categoryName.startsWith("관광명소")
                || categoryName.startsWith("문화시설")
                || categoryName.startsWith("여행")) {
            return Category.COURSE;
        }

        // 그 외는 전부 데이트 코스로 흡수
        return Category.COURSE;
    }
}
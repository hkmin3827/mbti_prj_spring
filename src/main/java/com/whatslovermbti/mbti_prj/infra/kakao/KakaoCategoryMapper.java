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
    /**
     * 카카오 category_name (예: "음식점 > 한식 > 백반,가정식")
     * → 우리 서비스 Category로 변환 (결과 분류용)
     */
    public static Category resolveCategory(String kakaoCategoryCode) {

        KakaoCategoryCode code = KakaoCategoryCode.from(kakaoCategoryCode);

        if (code == null) {
            return Category.COURSE;
        }

        return code.getCategory();
    }
}
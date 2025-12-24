package com.whatslovermbti.mbti_prj.infra.kakao;

import java.util.List;

public final class KakaoCourseCategory {

    private KakaoCourseCategory() {
    }

    public static List<String> codes() {
        return List.of(
                "AT4", // 관광명소
                "CT1", // 문화시설
                "AD5",  // 공연/전시
                "PK6"   // 공원
        );
    }
}
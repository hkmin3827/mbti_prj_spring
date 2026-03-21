package com.whatslovermbti.mbti_prj.infra.kakao;

import java.util.List;

public final class KakaoCourseCategory {

    private KakaoCourseCategory() {
    }

    public static List<String> codes() {
        return List.of(
                "AT4",
                "CT1",
                "AD5",
                "PK6"
        );
    }
}
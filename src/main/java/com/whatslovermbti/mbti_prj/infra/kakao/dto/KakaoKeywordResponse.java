package com.whatslovermbti.mbti_prj.infra.kakao.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class KakaoKeywordResponse {

    private List<Document> documents;
    private Meta meta;

    @Getter
    public static class Document {
        private String id;
        private String place_name;
        private String category_name;
        private String address_name;
        private String road_address_name;
        private String x;
        private String y;
    }

    @Getter
    public static class Meta {
        private int total_count;
        private boolean is_end;
    }
}
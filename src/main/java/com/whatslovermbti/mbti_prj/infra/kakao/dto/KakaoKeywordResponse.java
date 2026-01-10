package com.whatslovermbti.mbti_prj.infra.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class KakaoKeywordResponse {

    private List<Document> documents;
    private Meta meta;

    @Getter
    public static class Document {
        @JsonProperty("id")
        private String id;

        @JsonProperty("place_name")
        private String place_name;

        @JsonProperty("category_name")
        private String category_name;

        @JsonProperty("category_group_code")
        private String category_group_code;

        @JsonProperty("address_name")
        private String address_name;

        @JsonProperty("road_address_name")
        private String road_address_name;

        @JsonProperty("x")
        private String x;

        @JsonProperty("y")
        private String y;

        @JsonProperty("phone")
        private String phone;

        @JsonProperty("place_url")
        private String place_url;
    }

    @Getter
    public static class Meta {
        private int total_count;
        private boolean is_end;
    }
}
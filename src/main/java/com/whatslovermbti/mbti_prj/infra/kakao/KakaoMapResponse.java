package com.whatslovermbti.mbti_prj.infra.kakao;
// 카카오 맵에서 내려주는 JSON을 자바 객체로 받음
// 서비스로직에서 이걸 거리 계산, Category 매핑, 키워드 매핑, PlaceResDto로 변환
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class KakaoMapResponse {

    private List<Document> documents;
    private Meta meta;

    @Getter
    public static class Document {

        private String id;

        @JsonProperty("place_name")
        private String placeName;

        @JsonProperty("category_name")
        private String categoryName;

        @JsonProperty("address_name")
        private String addressName;

        @JsonProperty("road_address_name")
        private String roadAddressName;

        // longitude
        @JsonProperty("x")
        private String longitude;

        // latitude
        @JsonProperty("y")
        private String latitude;

        private String phone;

        @JsonProperty("place_url")
        private String placeUrl;
    }

    @Getter
    public static class Meta {

        @JsonProperty("total_count")
        private int totalCount;

        @JsonProperty("is_end")
        private boolean isEnd;
    }
}
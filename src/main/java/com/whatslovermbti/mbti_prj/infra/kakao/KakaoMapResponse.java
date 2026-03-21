package com.whatslovermbti.mbti_prj.infra.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.whatslovermbti.mbti_prj.infra.kakao.dto.KakaoKeywordResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Slf4j
public class KakaoMapResponse {

    private List<Document> documents = new ArrayList<>();
    private Meta meta;

    public static KakaoMapResponse from(KakaoKeywordResponse keywordResponse) {

        KakaoMapResponse response = new KakaoMapResponse();

        response.documents = keywordResponse.getDocuments().stream()
                .map(doc -> {
                    Document d = new Document();
                    d.id = doc.getId();
                    d.placeName = doc.getPlace_name();
                    d.categoryName = doc.getCategory_name();
                    d.categoryGroupCode = doc.getCategory_group_code();
                    d.addressName = doc.getAddress_name();
                    d.roadAddressName = doc.getRoad_address_name();
                    d.longitude = doc.getX();
                    d.latitude = doc.getY();

                    return d;
                })
                .collect(Collectors.toCollection(ArrayList::new));


        Meta meta = new Meta();
        meta.totalCount = keywordResponse.getMeta().getTotal_count();
        meta.isEnd = keywordResponse.getMeta().is_end();

        response.meta = meta;
        return response;
    }

    public void applyDocuments(List<Document> newDocs) {
        if (newDocs == null) {
            this.documents = new ArrayList<>();
            return;
        }
        this.documents = new ArrayList<>(newDocs);
    }

    @Getter @Setter
    public static class Document {

        @JsonProperty("id")
        private String id;

        @JsonProperty("place_name")
        private String placeName;

        @JsonProperty("category_name")
        private String categoryName;

        @JsonProperty("category_group_code")
        private String categoryGroupCode;

        @JsonProperty("address_name")
        private String addressName;

        @JsonProperty("road_address_name")
        private String roadAddressName;

        @JsonProperty("x")
        private String longitude;

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

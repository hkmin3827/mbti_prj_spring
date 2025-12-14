package com.whatslovermbti.mbti_prj.service.place;
// 카카오 JSON 응답을 PlaceResDto로 변환
// DB Place 변환 + Kakao Document 변환 둘 다 여기로 모으기
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatslovermbti.mbti_prj.constant.Category;
import com.whatslovermbti.mbti_prj.dto.place.PlaceResDto;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlaceMapper {
    // images(TEXT JSON배열) 파싱용 (DB Place -> DTO에서만 사용)
    private final ObjectMapper objectMapper = new ObjectMapper();


    public PlaceResDto fromKakao(
            KakaoMapResponse.Document doc,
            Category category,
            List<String> keywords,
            double distance
    ) {
        return new PlaceResDto(
                null,
                doc.getPlaceName(),
                category,
                doc.getRoadAddressName() != null ? doc.getRoadAddressName() : doc.getAddressName(),
                safeParseDouble(doc.getLatitude()),
                safeParseDouble(doc.getLongitude()),
                null,        // 카카오는 rating 기본 제공 안 함
                List.of(),    // 카카오는 imageUrls 기본 제공 안 함
                doc.getId(),
                keywords,
                distance
        );
    }

    public PlaceResDto fromEntity(
            Place place,
            double distance
    ) {
        List<String> keywords = place.getPlaceKeywords().stream()
                .map(pk -> pk.getKeyword().getName())
                .toList();

        return new PlaceResDto(
                place.getId(),
                place.getName(),
                place.getCategory(),
                place.getAddress(),
                place.getLatitude(),
                place.getLongitude(),
                place.getRating(),
                parseImages(place.getImages()), // ✅ 누락됐던 parseImages 제공
                place.getMapPlaceId(),
                keywords,
                distance
        );
    }

    private List<String> parseImages(String imagesJson) {
        if (imagesJson == null || imagesJson.isBlank()) return List.of();
        try {
            return objectMapper.readValue(imagesJson, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            // TODO: 로그 남기고 빈 리스트 반환(안전)
            return List.of();
        }
    }

    private Double safeParseDouble(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

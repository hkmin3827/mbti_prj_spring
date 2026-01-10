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
        String address =
                doc.getRoadAddressName() != null
                        ? doc.getRoadAddressName()
                        : doc.getAddressName();

        return PlaceResDto.fromKakao(
                doc.getPlaceName(),
                category,
                address,
                safeParseDouble(doc.getLatitude()),
                safeParseDouble(doc.getLongitude()),
                doc.getId(),      // kakaoPlaceId
                keywords,
                distance
        );
    }

    public PlaceResDto fromEntity(
            Place place,
            Double distance
    ) {
        List<String> keywords = place.getPlaceKeywords().stream()
                .map(pk -> pk.getKeyword().getName())
                .toList();

        List<String> imageUrls = parseImages(place.getImages());

        return PlaceResDto.fromEntity(
                place,
                imageUrls,
                keywords,
                distance
        );
    }

    private List<String> parseImages(String imagesJson) {
        if (imagesJson == null || imagesJson.isBlank()) return List.of();
        try {
            return objectMapper.readValue(imagesJson, new TypeReference<List<String>>() {});
        } catch (Exception e) {
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

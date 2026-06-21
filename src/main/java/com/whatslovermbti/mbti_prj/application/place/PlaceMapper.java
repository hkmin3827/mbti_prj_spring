package com.whatslovermbti.mbti_prj.application.place;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatslovermbti.mbti_prj.domain.place.dto.PlaceResDto;
import com.whatslovermbti.mbti_prj.domain.place.entity.Place;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PlaceMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public PlaceResDto fromBasicEntity(
            Place place,
            Double distance
    ) {
        List<String> imageUrls = parseImages(place.getImages());

        return PlaceResDto.fromEntity(
                place,
                imageUrls,
                List.of(),
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
}

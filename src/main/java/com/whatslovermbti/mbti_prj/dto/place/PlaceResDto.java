package com.whatslovermbti.mbti_prj.dto.place;

import com.whatslovermbti.mbti_prj.constant.Category;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.entity.Place;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public class PlaceResDto {
    private Long id;
    private String name;
    private Category category;
    private String address;
    private Double latitude;
    private Double longitude;

    // soft delete 상태
    private final boolean deleted;

    private Double rating;  // 우리회원 평가
    private List<String> imageUrls;   // 회원들이 올린 리뷰 사진

    private String kakaoPlaceId;

    // 추천/표시용
    private List<String> keywords;
    private Double distance;   // km (계산값)


    // mbtiContext별 저장 상태 (선택)
    private Set<MbtiContext> savedContexts;
    public void setSavedContexts(Set<MbtiContext> savedContexts) {
        this.savedContexts = savedContexts;
    }

    // 내부 공용 생성자
    private PlaceResDto(
            Long id,
            String name,
            Category category,
            String address,
            Double latitude,
            Double longitude,
            boolean deleted,
            Double rating,
            List<String> imageUrls,
            String kakaoPlaceId,
            List<String> keywords,
            Double distance
    ) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.deleted = deleted;
        this.rating = rating;
        this.imageUrls = imageUrls;
        this.kakaoPlaceId = kakaoPlaceId;
        this.keywords = keywords;
        this.distance = distance;
    }


    // 카카오 Document → DTO (추천 / 검색 단계)
    public static PlaceResDto fromKakao(
            String name,
            Category category,
            String address,
            Double latitude,
            Double longitude,
            String kakaoPlaceId,
            List<String> keywords,
            Double distance
    ) {
        return new PlaceResDto(
                null,               // DB id 없음
                name,
                category,
                address,
                latitude,
                longitude,
                false,              // 카카오 응답은 deleted 개념 없음
                null,               // rating 없음
                List.of(),           // image 없음
                kakaoPlaceId,
                keywords,
                distance
        );
    }


    // DB Place → DTO (마이페이지 / 저장 / 리뷰 조회)
        public static PlaceResDto fromEntity(
                Place place,
                List<String> imageUrls,
                List<String> keywords,
                Double distance
    ) {
            return new PlaceResDto(
                    place.getId(),
                    place.getName(),
                    place.getCategory(),
                    place.getAddress(),
                    place.getLatitude(),
                    place.getLongitude(),
                    place.isDeleted(),
                    place.getRating(),
                    imageUrls,
                    place.getKakaoPlaceId(),
                    keywords,
                    distance
            );
        }
}
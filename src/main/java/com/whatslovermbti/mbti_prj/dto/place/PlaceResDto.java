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

    private final boolean deleted;

    private Double rating;
    private List<String> imageUrls;

    private String kakaoPlaceId;

    private List<String> keywords;
    private Double distance;


    private Set<MbtiContext> likedContexts = Set.of();
    public void setLikedContexts(Set<MbtiContext> likedContexts) {
        this.likedContexts = likedContexts;
    }

    private MbtiContext bookmarkedContext;
    public void setBookmarkedContext(MbtiContext bookmarkedContext) {this.bookmarkedContext = bookmarkedContext;}

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
                null,
                name,
                category,
                address,
                latitude,
                longitude,
                false,
                null,
                List.of(),
                kakaoPlaceId,
                keywords,
                distance
        );
    }


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
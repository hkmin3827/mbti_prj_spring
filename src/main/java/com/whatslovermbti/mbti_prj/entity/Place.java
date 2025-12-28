package com.whatslovermbti.mbti_prj.entity;

import com.whatslovermbti.mbti_prj.constant.Category;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Where(clause = "deleted = false")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String address;
    private String roadAddress;
    private Double longitude;   // 경도
    private Double latitude;   // 위도

    private Double rating;

    @Column(columnDefinition = "TEXT")
    private String images;   // 이미지 URL JSON 배열

    @Column(columnDefinition = "TEXT")
    private String description;

    private String kakaoPlaceId;   // 카카오 PlaceId

    // 중간 연결 엔티티
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private List<PlaceKeyword> placeKeywords = new ArrayList<>();

    // 키워드 추출 메서드
    @Transient
    public List<Keyword> getKeywords() {
        return placeKeywords.stream()
                .map(PlaceKeyword::getKeyword)
                .toList();
    }

    private boolean deleted = false;

    private LocalDateTime deletedAt;

    public void softDelete() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void restore() {
        this.deleted = false;
        this.deletedAt = null;
    }

    public KakaoMapResponse.Document toKakaoDocument() {

        KakaoMapResponse.Document doc = new KakaoMapResponse.Document();

        doc.setId(this.kakaoPlaceId);
        doc.setPlaceName(this.name);
        doc.setAddressName(this.address);
        doc.setLongitude(String.valueOf(this.longitude));
        doc.setLatitude(String.valueOf(this.latitude));

        // 카카오 스타일 category_name 형태로 복원
        doc.setCategoryName(this.category.name());

        return doc;
    }
}

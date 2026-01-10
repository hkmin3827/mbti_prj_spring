package com.whatslovermbti.mbti_prj.entity;

import com.whatslovermbti.mbti_prj.constant.Category;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
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
    private String telnum;

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
    public void updateRating(double avg) {
        this.rating = Math.round(avg * 10) / 10.0;
    }
}

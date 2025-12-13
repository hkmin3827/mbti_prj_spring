package com.whatslovermbti.mbti_prj.entity;

import com.whatslovermbti.mbti_prj.constant.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private Double longitude;   // 경도
    private Double latitude;   // 위도

    private Double rating;

    @Column(columnDefinition = "TEXT")
    private String images;   // 이미지 URL JSON 배열

    @Column(columnDefinition = "TEXT")
    private String description;

    private String naverReservationUrl;

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

}

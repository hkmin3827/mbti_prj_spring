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

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private List<PlaceKeyword> placeKeywords = new ArrayList<>();

}

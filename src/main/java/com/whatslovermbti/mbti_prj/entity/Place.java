package com.whatslovermbti.mbti_prj.entity;

import com.whatslovermbti.mbti_prj.constant.Category;
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
    private Double longitude;
    private Double latitude;

    private Double rating;

    @Column(columnDefinition = "TEXT")
    private String images;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String kakaoPlaceId;
    private String telnum;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private List<PlaceKeyword> placeKeywords = new ArrayList<>();

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

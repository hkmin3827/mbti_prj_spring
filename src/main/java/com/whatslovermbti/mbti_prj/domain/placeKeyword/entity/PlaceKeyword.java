package com.whatslovermbti.mbti_prj.domain.placeKeyword.entity;

import com.whatslovermbti.mbti_prj.domain.place.entity.Place;
import com.whatslovermbti.mbti_prj.domain.keyword.entity.Keyword;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class PlaceKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    private Keyword keyword;

    private int weight;


    public PlaceKeyword(Place place, Keyword keyword, int weight) {
        this.place = place;
        this.keyword = keyword;
        this.weight = weight;
    }

    public void increaseWeight(int delta) {
        this.weight += delta;
    }
}

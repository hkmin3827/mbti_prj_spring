package com.whatslovermbti.mbti_prj.domain.keyword.entity;

import com.whatslovermbti.mbti_prj.global.constant.MbtiAxis;
import com.whatslovermbti.mbti_prj.domain.placeKeyword.entity.PlaceKeyword;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MbtiAxis axis;

    @OneToMany(mappedBy = "keyword")
    private List<PlaceKeyword> placeKeywords = new ArrayList<>();

    public Keyword(String name, MbtiAxis axis) {
        this.name = name;
        this.axis = axis;
    }
}

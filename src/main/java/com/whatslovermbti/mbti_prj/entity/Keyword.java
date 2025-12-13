package com.whatslovermbti.mbti_prj.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 내부 표준 키워드
    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "keyword")
    private List<PlaceKeyword> placeKeywords = new ArrayList<>();
}

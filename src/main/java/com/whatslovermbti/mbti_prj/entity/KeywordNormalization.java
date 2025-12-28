package com.whatslovermbti.mbti_prj.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KeywordNormalization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 외부에서 들어오는 키워드
    @Column(nullable = false)
    private String rawKeyword;

    // 내부 표준 키워드
    @ManyToOne(fetch = FetchType.LAZY)
    private Keyword standardKeyword;

    public KeywordNormalization(
            String rawKeyword,
            Keyword standardKeyword
    ) {
        this.rawKeyword = rawKeyword;
        this.standardKeyword = standardKeyword;
    }
}
package com.whatslovermbti.mbti_prj.entity;
// 추후 지도 외부 API 사용 시 키워드 받아올때 내부 표준 키워드로 정규화

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
}
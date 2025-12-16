package com.whatslovermbti.mbti_prj.entity;

import com.whatslovermbti.mbti_prj.constant.MbtiAxis;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// MBTI 기반 초기 가중치 : 초기 추천설정에 필요. 추후 좋아요/싫어요 기반으로 키워드선호도 조정
@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"mbti_axis", "keyword_id"}
        )
)
public class MbtiKeywordWeight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mbti_axis", nullable = false)
    @Enumerated(EnumType.STRING)
    private MbtiAxis mbtiAxis; // I, E, N, S, T, F, J, P

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;

    @Column(nullable = false)
    private int weight; // -5 ~ +5
}

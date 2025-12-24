package com.whatslovermbti.mbti_prj.entity;

import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "keyword_id", "target_mbti"}
        )
)
public class UserKeywordPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Keyword keyword;

    @Enumerated(EnumType.STRING)
    MbtiContext targetMbti;

    // 선호 점수 (-100 ~ +100 권장)
    private int score;
}
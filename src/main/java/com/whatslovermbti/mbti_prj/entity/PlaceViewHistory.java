package com.whatslovermbti.mbti_prj.entity;

import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "place_id", "target_mbti"}
        )
)
@Entity
@Getter
@NoArgsConstructor
public class PlaceViewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Place place;

    @Column(nullable = false, updatable = false)
    private LocalDateTime viewedAt;
    @PrePersist

    public void onCreate() {
        this.viewedAt = LocalDateTime.now();
    }

    public PlaceViewHistory(User user, Place place, MbtiContext targetMbti) {
        this.user = user;
        this.place = place;
        this.targetMbti = targetMbti;
        this.viewedAt = LocalDateTime.now();
    }

    @Enumerated(EnumType.STRING)
    MbtiContext targetMbti;

    public void updateViewedAt() {
        this.viewedAt = LocalDateTime.now();
    }
}

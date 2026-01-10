package com.whatslovermbti.mbti_prj.entity;

import com.whatslovermbti.mbti_prj.constant.ActionType;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "place_id", "target_mbti"}
        )
)
@Getter @Setter
@NoArgsConstructor
public class PlaceReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Place place;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType type; // LIKE / DISLIKE

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public PlaceReaction(User user, Place place, ActionType type, MbtiContext targetMbti) {
        this.user = user;
        this.place = place;
        this.type = type;
        this.targetMbti = targetMbti;
    }
    @Enumerated(EnumType.STRING)
    private MbtiContext targetMbti;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

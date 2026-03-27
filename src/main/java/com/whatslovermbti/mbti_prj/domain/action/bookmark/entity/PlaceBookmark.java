package com.whatslovermbti.mbti_prj.domain.action.bookmark.entity;

import com.whatslovermbti.mbti_prj.global.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.domain.place.entity.Place;
import com.whatslovermbti.mbti_prj.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "place_id", "target_mbti"}
        )
)
@Getter
@NoArgsConstructor
public class PlaceBookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Place place;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public PlaceBookmark(User user, Place place, MbtiContext targetMbti) {
        this.user = user;
        this.place = place;
        this.targetMbti = targetMbti;
    }
    @Enumerated(EnumType.STRING)
    MbtiContext targetMbti;
}

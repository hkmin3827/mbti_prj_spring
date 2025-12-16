package com.whatslovermbti.mbti_prj.entity;
// 클릭한 플레이스 (CLICK)

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
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

    public PlaceViewHistory(User user, Place place) {
        this.user = user;
        this.place = place;
    }
}

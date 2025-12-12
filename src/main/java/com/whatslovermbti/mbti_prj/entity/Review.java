package com.whatslovermbti.mbti_prj.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Place place;

    private int rating;

    @Column(columnDefinition = "TEXT")
    private String content;

    private boolean verified; // 영수증 인증 여부

    private String receiptImageUrl;

    private LocalDateTime createTime;
    @PrePersist
    public void onCreate() {
    this.createTime = LocalDateTime.now();
    }

}

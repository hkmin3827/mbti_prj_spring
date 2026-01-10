package com.whatslovermbti.mbti_prj.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "receipt_hash"}
                )
        }
)
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

    @Column(nullable = true)
    private String reviewImageUrl;

    private boolean verified; // 영수증 인증 여부
    @Column(nullable = true)
    private String receiptImageUrl;
    @Column(length = 64)
    private String receiptHash;

    private Integer placeMatchScore;

    private long viewCount = 0;

    private LocalDateTime createdAt;
    @PrePersist
    public void onCreate() {
    this.createdAt = LocalDateTime.now();
    }

}

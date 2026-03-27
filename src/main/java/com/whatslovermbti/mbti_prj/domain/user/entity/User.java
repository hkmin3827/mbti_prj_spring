package com.whatslovermbti.mbti_prj.domain.user.entity;

import com.whatslovermbti.mbti_prj.global.constant.Provider;
import com.whatslovermbti.mbti_prj.global.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"provider", "oauth_id"})
        }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;   // 소셜 계정 이메일 포함, nullable = true

    @Column(length = 30)
    private String name;

    private String profileImage;
    private String password;   // 소셜 로그인 계정은 null

    @Column(nullable = false)
    private Provider provider;
    private String oauthId;

    private String mbti;
    private String partnerMbti;  // optional

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;


    @Column(length = 30)
    private String telnum;

    @Column(nullable = false)
    private boolean profileCompleted = false;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    private LocalDateTime lastLogoutAt;

    public void logout() {
        this.lastLogoutAt = LocalDateTime.now();
    }

    public void updateBasicProfile(String name, String profileImage, String telnum) {
        this.name =name;
        this.profileImage = profileImage;
        this.telnum = telnum;
    }
    public void updateMbtiProfile(String mbti, String partnerMbti){
        this.mbti = mbti;
        this.partnerMbti = partnerMbti;
    }

    public void profileCompleted(){this.profileCompleted = true;}

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }
}

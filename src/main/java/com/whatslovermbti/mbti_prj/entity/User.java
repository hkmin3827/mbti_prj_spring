package com.whatslovermbti.mbti_prj.entity;

import com.whatslovermbti.mbti_prj.constant.Provider;
import com.whatslovermbti.mbti_prj.constant.Role;
import com.whatslovermbti.mbti_prj.dto.user.ProfileReqDto;
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
    private String email;   // 소셜 계정 이메일 포함, 카카오같이 이메일 없을 수 있으므로 nullable = true

    @Column(length = 30)
    private String name;

    private String profileImage;
    private String password;   // 소셜 로그인 계정은 null

    @Column(nullable = false)
    private Provider provider;   // 로그인 종류 (Local/google/kakao/naver)
    private String oauthId;   // 필수. 소셜 로그인 고유값

    private String mbti;
    private String partnerMbti;   // 상대 MBTI (옵션)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;     // USER / ADMIN


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

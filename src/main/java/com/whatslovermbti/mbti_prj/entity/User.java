package com.whatslovermbti.mbti_prj.entity;

import com.whatslovermbti.mbti_prj.constant.Provider;
import com.whatslovermbti.mbti_prj.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;   // 소셜 계정 이메일 포함, 카카오같이 이메일 없을 수 있으므로 nullable = true
    private String name;
    private String profileImage;
    private String password;   // 소셜 로그인 계정은 null

    @Column(nullable = false)
    private Provider provider;   // 로그인 종류 (Local/google/kakao/naver)
    private String providerId;   // 필수. 소셜 로그인 고유값

    private String mbti;
    private String partnerMbti;   // 상대 MBTI (옵션)

    // 이런식으로 선택할것임 **
//    MBTI baseMbti =
//            request.isPartnerMode()
//                    ? user.getPartnerMbti()
//                    : user.getMbti();


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;     // USER / ADMIN


    @Column(length = 30)
    private String telnum;

}

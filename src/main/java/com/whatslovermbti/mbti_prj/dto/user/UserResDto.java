package com.whatslovermbti.mbti_prj.dto.user;

import com.whatslovermbti.mbti_prj.constant.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResDto {
    private String email;
    private Provider provider;
    private String name;
    private String oauthId;
    private Long id;
    private String mbti;
    private String partnerMbti;
    private String profileImage;
    private String telnum;
    private Boolean profileCompleted;
    private Boolean isActive;
    private LocalDateTime createdAt;
}

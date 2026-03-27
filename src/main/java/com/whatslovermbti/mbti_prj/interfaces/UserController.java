package com.whatslovermbti.mbti_prj.interfaces;

import com.whatslovermbti.mbti_prj.domain.user.dto.UserResDto;
import com.whatslovermbti.mbti_prj.domain.user.entity.User;
import com.whatslovermbti.mbti_prj.global.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    @GetMapping("/me")
    public UserResDto me(@AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userDetails.getUser();

        return new UserResDto(
                user.getEmail(),
                user.getProvider(),
                user.getName(),
                user.getOauthId(),
                user.getId(),
                user.getMbti(),
                user.getPartnerMbti(),
                user.getProfileImage(),
                user.getTelnum(),
                user.isProfileCompleted(),
                user.isActive(),
                user.getCreatedAt(),
                user.getRole()
        );
    }
}
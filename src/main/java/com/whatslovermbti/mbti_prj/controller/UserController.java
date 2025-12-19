package com.whatslovermbti.mbti_prj.controller;

import com.whatslovermbti.mbti_prj.dto.user.UserResDto;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.security.auth.CustomUserDetails;
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
                user.getId()
        );
    }
}

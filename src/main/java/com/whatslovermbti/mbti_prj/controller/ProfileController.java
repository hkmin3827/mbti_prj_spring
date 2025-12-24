package com.whatslovermbti.mbti_prj.controller;

import com.whatslovermbti.mbti_prj.dto.user.ProfileReqDto;
import com.whatslovermbti.mbti_prj.security.auth.CustomUserDetails;
import com.whatslovermbti.mbti_prj.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/profile")
public class ProfileController {
    private final UserProfileService userProfileService;

    @PostMapping
    public ResponseEntity<Void> createProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ProfileReqDto dto
    ) {
        userProfileService.createProfile(userDetails.getUser().getId(), dto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<Void> modifyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ProfileReqDto dto
    ) {
        userProfileService.modifyProfile(userDetails.getUser().getId(), dto);
        return ResponseEntity.ok().build();
    }
}

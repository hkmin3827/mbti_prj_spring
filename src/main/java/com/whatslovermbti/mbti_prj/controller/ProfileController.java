package com.whatslovermbti.mbti_prj.controller;

import com.whatslovermbti.mbti_prj.dto.user.BasicProfileReq;
import com.whatslovermbti.mbti_prj.dto.user.MbtiProfileReq;
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

    @PostMapping("/create/basic")
    public ResponseEntity<Void> createBasicProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BasicProfileReq dto
    ) {
        userProfileService.createBasicProfile(userDetails.getUser().getId(), dto);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/create/mbti")
    public ResponseEntity<Void> createMbtiProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MbtiProfileReq dto
    ) {
        userProfileService.createMbtiProfile(userDetails.getUser().getId(), dto);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/update/basic")
    public ResponseEntity<Void> modifyBasicProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BasicProfileReq dto
    ) {
        userProfileService.modifyBasicProfile(userDetails.getUser().getId(), dto);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/update/mbti")
    public ResponseEntity<Void> modifyMbtiProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MbtiProfileReq dto
    ) {
        userProfileService.modifyMbtiProfile(userDetails.getUser().getId(), dto);
        return ResponseEntity.ok().build();
    }
}

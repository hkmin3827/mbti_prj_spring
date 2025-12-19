package com.whatslovermbti.mbti_prj.controller;

import com.whatslovermbti.mbti_prj.dto.auth.LoginReqDto;
import com.whatslovermbti.mbti_prj.dto.auth.SignUpReqDto;
import com.whatslovermbti.mbti_prj.dto.auth.TokenResDto;
import com.whatslovermbti.mbti_prj.dto.auth.WithdrawReqDto;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.security.auth.CustomUserDetails;
import com.whatslovermbti.mbti_prj.security.jwt.JwtProvider;
import com.whatslovermbti.mbti_prj.service.AuthService;
import com.whatslovermbti.mbti_prj.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final CurrentUserService currentUserService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpReqDto dto) {
        authService.signup(dto);
        return ResponseEntity.ok("회원가입 완료");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResDto> login(@RequestBody LoginReqDto req) {

        User user = authService.login(req);

        String token = jwtProvider.createToken(user.getId());

        return ResponseEntity.ok(new TokenResDto(token));

    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<Void> withdraw(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody(required = false) WithdrawReqDto dto
    ) {
        User user = currentUserService.getCurrentUser(userDetails);
        authService.withdraw(user, dto);
        return ResponseEntity.noContent().build();
    }

}

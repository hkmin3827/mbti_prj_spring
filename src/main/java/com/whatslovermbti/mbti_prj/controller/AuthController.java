package com.whatslovermbti.mbti_prj.controller;

import com.whatslovermbti.mbti_prj.dto.auth.LoginReqDto;
import com.whatslovermbti.mbti_prj.dto.auth.SignUpReqDto;
import com.whatslovermbti.mbti_prj.dto.auth.TokenResDto;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.security.jwt.JwtProvider;
import com.whatslovermbti.mbti_prj.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpReqDto dto) {
        userService.signup(dto);
        return ResponseEntity.ok("회원가입 완료");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResDto> login(@RequestBody LoginReqDto req) {

        User user = userService.login(req);

        String token = jwtProvider.createToken(user.getId());

        return ResponseEntity.ok(new TokenResDto(token));

    }



    // 로그인 정보 필요한 컨트롤러 마다 넣어주기
//    @GetMapping("/me")
//    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal CustomUserDetails user) {
//        return ResponseEntity.ok(new UserResponse(user.getUser()));
//    }
}

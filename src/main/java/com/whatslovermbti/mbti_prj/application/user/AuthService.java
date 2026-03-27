package com.whatslovermbti.mbti_prj.application.user;

import com.whatslovermbti.mbti_prj.global.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.global.constant.Provider;
import com.whatslovermbti.mbti_prj.global.constant.Role;
import com.whatslovermbti.mbti_prj.domain.user.dto.auth.LoginReqDto;
import com.whatslovermbti.mbti_prj.domain.user.dto.auth.SignUpReqDto;
import com.whatslovermbti.mbti_prj.domain.user.dto.auth.WithdrawReqDto;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoUnlinkClient;
import com.whatslovermbti.mbti_prj.global.security.oauth.userInfo.OAuthUserInfo;
import com.whatslovermbti.mbti_prj.domain.user.entity.User;
import com.whatslovermbti.mbti_prj.global.exception.CustomException;
import com.whatslovermbti.mbti_prj.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KakaoUnlinkClient kakaoUnlinkClient;

    public void signup(SignUpReqDto dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATED);
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setProvider(Provider.LOCAL);
        user.setName(dto.getName());
        user.setRole(Role.USER);
        user.setTelnum(dto.getTelnum());

        userRepository.save(user);
    }

    public User login(LoginReqDto dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        return user;
    }

    @Transactional
    public User signupOrLoginOAuth(OAuthUserInfo info) {
        return userRepository
                .findByProviderAndOauthId(
                        info.getProvider(),
                        info.getProviderId()
                )
                .orElseGet(() -> {
                    User user = new User();
                    user.setProvider(info.getProvider());
                    user.setOauthId(info.getProviderId());
                    user.setEmail(info.getEmail());
                    user.setName(info.getName());
                    user.setRole(Role.USER);
                    return userRepository.save(user);
                });
    }

    @Transactional
    public void withdraw(User user, WithdrawReqDto dto) {
        if (dto == null || !"탈퇴합니다".equals(dto.getConfirmText())) {
            throw new IllegalArgumentException("탈퇴 확인 문구가 올바르지 않습니다.");
        }

        if (user.getProvider() == Provider.KAKAO) {
            try {
                kakaoUnlinkClient.unlink(user.getOauthId());
            } catch (Exception e) {
                log.warn("Kakao unlink failed", e);
            }
        }

        userRepository.delete(user);
    }
}
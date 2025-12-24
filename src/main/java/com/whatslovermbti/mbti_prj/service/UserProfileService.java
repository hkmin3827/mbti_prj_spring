package com.whatslovermbti.mbti_prj.service;

import com.whatslovermbti.mbti_prj.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.dto.user.ProfileReqDto;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.exception.CustomException;
import com.whatslovermbti.mbti_prj.repository.UserRepository;
import com.whatslovermbti.mbti_prj.util.MbtiValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService {

    private final UserRepository userRepository;

    public void createProfile(Long userId, ProfileReqDto dto){

        User user = userRepository.findById(userId).orElseThrow(
                ()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (user.getMbti() != null) {
            throw new CustomException(ErrorCode.PROFILE_ALREADY_EXISTS);
        }
        if (dto.getMbti() == null || dto.getTelnum() == null) {
            throw new CustomException(ErrorCode.PROFILE_NOT_COMPLETED);
        }

        MbtiValidator.validate(dto.getMbti());
        user.updateProfile(
                dto.getName(),
                dto.getProfileImage(),
                dto.getMbti(),
                dto.getPartnerMbti(),
                dto.getTelnum()
        );
    }

    public void modifyProfile(Long userId, ProfileReqDto dto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        // PATCH 병합 (null = 유지)
        String finalName =
                dto.getName() != null ? dto.getName() : user.getName();

        String finalProfileImage =
                dto.getProfileImage() != null ? dto.getProfileImage() : user.getProfileImage();

        String finalMbti =
                dto.getMbti() != null ? dto.getMbti() : user.getMbti();

        String finalPartnerMbti =
                dto.getPartnerMbti() != null ? dto.getPartnerMbti() : user.getPartnerMbti();

        String finalTelnum =
                dto.getTelnum() != null ? dto.getTelnum() : user.getTelnum();

        if (finalMbti == null || finalTelnum == null) {
            throw new CustomException(ErrorCode.PROFILE_NOT_COMPLETED);
        }

        MbtiValidator.validate(dto.getMbti());

        user.updateProfile(
                finalName,
                finalProfileImage,
                finalMbti,
                finalPartnerMbti,
                finalTelnum
        );
    }
}

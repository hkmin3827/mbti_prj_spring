package com.whatslovermbti.mbti_prj.service;

import com.whatslovermbti.mbti_prj.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.dto.user.BasicProfileReq;
import com.whatslovermbti.mbti_prj.dto.user.MbtiProfileReq;
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

    public void createBasicProfile(Long userId, BasicProfileReq dto){

        User user = userRepository.findById(userId).orElseThrow(
                ()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (dto.getName() == null || dto.getTelnum() == null) {
            throw new CustomException(ErrorCode.PROFILE_NOT_COMPLETED);
        }

        user.updateBasicProfile(
                dto.getName(),
                dto.getProfileImage(),
                dto.getTelnum()
        );

        user.profileCompleted();
    }

    public void createMbtiProfile(Long userId, MbtiProfileReq dto){
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.getMbti()!= null){
            throw new CustomException(ErrorCode.PROFILE_ALREADY_EXISTS);
        }

        if (dto.getMbti() == null){
            throw new CustomException(ErrorCode.PROFILE_NOT_COMPLETED);
        }

        MbtiValidator.validate(dto.getMbti());
        user.updateMbtiProfile(
                dto.getMbti(),
                dto.getPartnerMbti()
        );
    }
    public void modifyBasicProfile(Long userId,BasicProfileReq dto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        // PATCH 병합 (null = 유지)
        String finalName =
                dto.getName() != null ? dto.getName() : user.getName();

        String finalProfileImage =
                dto.getProfileImage() != null ? dto.getProfileImage() : user.getProfileImage();

        String finalTelnum =
                dto.getTelnum() != null ? dto.getTelnum() : user.getTelnum();

        if (finalName == null || finalTelnum == null) {
            throw new CustomException(ErrorCode.PROFILE_NOT_COMPLETED);
        }

        user.updateBasicProfile(
                finalName,
                finalProfileImage,
                finalTelnum
        );
    }
    public void modifyMbtiProfile(Long userId, MbtiProfileReq dto){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        String finalMbti =
                dto.getMbti() != null ? dto.getMbti() : user.getMbti();

        String finalPartnerMbti =
                dto.getPartnerMbti() != null ? dto.getPartnerMbti() : user.getPartnerMbti();

        if (finalMbti == null) {
            throw new CustomException(ErrorCode.PROFILE_NOT_COMPLETED);
        }

        MbtiValidator.validate(dto.getMbti());

        user.updateMbtiProfile(
                dto.getMbti(),
                dto.getPartnerMbti()
        );

    }
}

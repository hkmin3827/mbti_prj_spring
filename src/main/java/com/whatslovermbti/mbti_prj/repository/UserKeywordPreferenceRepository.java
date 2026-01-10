package com.whatslovermbti.mbti_prj.repository;

import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.entity.UserKeywordPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserKeywordPreferenceRepository
        extends JpaRepository<UserKeywordPreference, Long> {
    List<UserKeywordPreference> findAllByUser(User user);

    Optional<UserKeywordPreference> findByUserAndKeywordAndTargetMbti(
            User user,
            Keyword keyword,
            MbtiContext targetMbti
    );

    List<UserKeywordPreference> findByUserAndTargetMbti(User user, MbtiContext targetMbti);
}
package com.whatslovermbti.mbti_prj.repository;

import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.entity.UserKeywordPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserKeywordPreferenceRepository
        extends JpaRepository<UserKeywordPreference, Long> {

    // 특정 유저 + 특정 키워드 선호도 조회
    Optional<UserKeywordPreference> findByUserAndKeyword(User user, Keyword keyword);

    // 유저가 가진 모든 키워드 선호도
    List<UserKeywordPreference> findByUser(User user);

    // 키워드 기준으로 유저들 선호도 조회 (통계/AI용)
    List<UserKeywordPreference> findByKeyword(Keyword keyword);

    List<UserKeywordPreference> findAllByUser(User user);
}
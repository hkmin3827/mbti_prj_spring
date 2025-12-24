package com.whatslovermbti.mbti_prj.repository;

import com.whatslovermbti.mbti_prj.constant.ActionType;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.entity.UserActionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface UserActionLogRepository extends JpaRepository<UserActionLog, Long> {
    boolean existsByUserAndPlaceAndActionTypeAndTargetMbtiAndCreatedAtBetween(
            User user,
            Place place,
            ActionType actionType,
            MbtiContext targetMbti,
            LocalDateTime start,
            LocalDateTime end
    );
}

package com.whatslovermbti.mbti_prj.repository;

import com.whatslovermbti.mbti_prj.constant.ActionType;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.dto.place.PlaceViewCountDto;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.entity.UserActionLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserActionLogRepository extends JpaRepository<UserActionLog, Long> {
    boolean existsByUserAndPlaceAndActionTypeAndTargetMbtiAndCreatedAtBetween(
            User user,
            Place place,
            ActionType actionType,
            MbtiContext targetMbti,
            LocalDateTime start,
            LocalDateTime end
    );

    void deleteByPlaceId(Long placeId);

    @Query("""
        select new com.whatslovermbti.mbti_prj.dto.place.PlaceViewCountDto(
            p.id,
            p.name,
            p.address,
            p.roadAddress,
            p.category,
            p.rating,
            count(l)
        )
        from UserActionLog l
        join l.user u
        join l.place p
        where l.actionType = :actionType
          and p.deleted = false
          and (
            (l.targetMbti = 'SELF' and u.mbti = :mbti)
            or
            (l.targetMbti = 'PARTNER' and u.partnerMbti = :mbti)
          )
        group by p.id, p.name, p.address, p.roadAddress, p.category, p.rating
        order by count(l) desc
        """)
    List<PlaceViewCountDto> findMostViewedPlacesByEffectiveMbti(
            @Param("mbti") String mbti,
            @Param("actionType") ActionType actionType,
            Pageable pageable)
            ;
}

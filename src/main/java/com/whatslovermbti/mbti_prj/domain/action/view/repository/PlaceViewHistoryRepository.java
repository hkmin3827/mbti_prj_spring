package com.whatslovermbti.mbti_prj.domain.action.view.repository;

import com.whatslovermbti.mbti_prj.global.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.domain.place.entity.Place;
import com.whatslovermbti.mbti_prj.domain.action.view.entity.PlaceViewHistory;
import com.whatslovermbti.mbti_prj.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PlaceViewHistoryRepository extends JpaRepository<PlaceViewHistory, Long> {

    boolean existsByUserAndPlaceAndTargetMbtiAndViewedAtBetween(
            User user,
            Place place,
            MbtiContext targetMbti,
            LocalDateTime start,
            LocalDateTime end
    );

    @Query("""
        select v
        from PlaceViewHistory v
        join fetch v.place p
        where v.user.id = :userId
          and p.deleted = false
          and v.viewedAt = (
              select max(v2.viewedAt)
              from PlaceViewHistory v2
              join v2.place p2
              where v2.user.id = :userId
                and v2.place.id = v.place.id
                and p2.deleted = false
          )
        order by v.viewedAt desc
    """)
    List<PlaceViewHistory> findLatestViewsPerPlace(Long userId);

    void deleteByPlaceId(Long placeId);

    @Query("""
        select v.id
        from PlaceViewHistory v
        where v.user.id = :userId
          and v.targetMbti = :targetMbti
        order by v.viewedAt desc
    """)
    List<Long> findIdsByUserAndTargetMbtiOrderByViewedAtDesc(
            Long userId,
            MbtiContext targetMbti
    );

    @Modifying
        @Query("""
        delete from PlaceViewHistory v
        where v.id in :ids
    """)
    void deleteByIds(List<Long> ids);

    // 특정 장소의 조회 기록 삭제 (user 기준, context 무시)
    @Modifying
    @Query("""
        delete from PlaceViewHistory v
        where v.user.id = :userId
          and v.place.id = :placeId
    """)
    void deleteByUserIdAndPlaceId(Long userId, Long placeId);

    Optional<PlaceViewHistory> findByUserAndPlaceAndTargetMbti(
            User user,
            Place place,
            MbtiContext targetMbti
    );

    @Modifying
    @Query("DELETE FROM PlaceViewHistory pvh WHERE pvh.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}

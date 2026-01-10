package com.whatslovermbti.mbti_prj.repository;

import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.dto.place.PlaceViewCountDto;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.PlaceViewHistory;
import com.whatslovermbti.mbti_prj.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PlaceViewHistoryRepository extends JpaRepository<PlaceViewHistory, Long> {
    // VIEW 가중치 반영은 하루에 1회 조회된 것으로만 계산
    boolean existsByUserAndPlaceAndTargetMbtiAndViewedAtBetween(
            User user,
            Place place,
            MbtiContext targetMbti,
            LocalDateTime start,
            LocalDateTime end
    );

    // 최근 조회한 장소들 (최신순)
    @Query("""
        select v
        from PlaceViewHistory v
        join v.place p
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

}

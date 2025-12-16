package com.whatslovermbti.mbti_prj.repository;

import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.PlaceViewHistory;
import com.whatslovermbti.mbti_prj.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface PlaceViewHistoryRepository extends JpaRepository<PlaceViewHistory, Long> {
    @Modifying
    @Query("""
        DELETE FROM PlaceViewHistory v
        WHERE v.user.id = :userId
        AND v.id NOT IN (
            SELECT v2.id FROM PlaceViewHistory v2
            WHERE v2.user.id = :userId
            ORDER BY v2.viewedAt DESC
            LIMIT :limit
        )
        """)
    void deleteOldViews(Long userId, int limit);

    // VIEW 가중치 반영은 하루에 1회 조회된 것으로만 계산
    boolean existsByUserAndPlaceAndViewedAtBetween(
            User user,
            Place place,
            LocalDateTime start,
            LocalDateTime end
    );
}

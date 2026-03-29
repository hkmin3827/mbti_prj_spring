package com.whatslovermbti.mbti_prj.domain.action.bookmark.repository;

import com.whatslovermbti.mbti_prj.global.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.domain.action.bookmark.dto.PlaceBookmarkContextRow;
import com.whatslovermbti.mbti_prj.domain.action.bookmark.entity.PlaceBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceBookmarkRepository extends JpaRepository<PlaceBookmark, Long> {
    boolean existsByUserIdAndPlaceIdAndTargetMbti(Long userId, Long placeId, MbtiContext targetMbti);

    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);

    Optional<PlaceBookmark> findByUserIdAndPlaceId(Long userId, Long placeId);

    void deleteByPlaceId(Long placeId);

    @Query("""
        select new com.whatslovermbti.mbti_prj.domain.action.bookmark.dto.PlaceBookmarkContextRow(
            pb.place.id,
            pb.targetMbti
        )
        from PlaceBookmark pb
        where pb.user.id = :userId
        order by pb.createdAt desc
    """)
    List<PlaceBookmarkContextRow> findBookmarkContextsByUser(
            @Param("userId") Long userId
    );

    @Modifying
    @Query("DELETE FROM PlaceBookmark pb WHERE pb.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}

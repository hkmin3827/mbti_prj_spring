package com.whatslovermbti.mbti_prj.repository;

import com.whatslovermbti.mbti_prj.constant.ActionType;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.dto.place.PlaceLikedContextRow;
import com.whatslovermbti.mbti_prj.entity.PlaceReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PlaceReactionRepository extends JpaRepository<PlaceReaction, Long> {
    Optional<PlaceReaction> findByUserIdAndPlaceIdAndTargetMbti(Long userId, Long placeId, MbtiContext targetMbti);

    Optional<PlaceReaction> findByUserIdAndPlaceIdAndTargetMbtiAndType(
            Long userId,
            Long placeId,
            MbtiContext targetMbti,
            ActionType type
    );

    void deleteByUserIdAndPlaceIdAndTargetMbtiAndType(
            Long userId,
            Long placeId,
            MbtiContext targetMbti,
            ActionType type
    );

    @Query("""
        select pr
        from PlaceReaction pr
        join pr.place p
        where pr.user.id = :userId
          and pr.type = :type
          and p.deleted = false
          and pr.createdAt = (
              select max(pr2.createdAt)
              from PlaceReaction pr2
              join pr2.place p2
              where pr2.user.id = :userId
                and pr2.place.id = pr.place.id
                and pr2.type = :type
                and p2.deleted = false
          )
        order by pr.createdAt desc
    """)
    List<PlaceReaction> findLatestReactionsPerPlace(
            Long userId,
            ActionType type
    );

    void deleteByPlaceId(Long placeId);

    @Query("""
    select new com.whatslovermbti.mbti_prj.dto.place.PlaceLikedContextRow(
        pr.place.id,
        pr.targetMbti,
        max(pr.updatedAt)
    )
    from PlaceReaction pr
    where pr.user.id = :userId
      and pr.type = :type
    group by pr.place.id, pr.targetMbti
    order by max(pr.updatedAt) desc
""")
    List<PlaceLikedContextRow> findLikedContextsByUser(
            @Param("userId") Long userId,
            @Param("type") ActionType type
    );


}

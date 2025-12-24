package com.whatslovermbti.mbti_prj.repository;

import com.whatslovermbti.mbti_prj.constant.ActionType;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.entity.PlaceReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PlaceReactionRepository extends JpaRepository<PlaceReaction, Long> {
    Optional<PlaceReaction> findByUserIdAndPlaceIdAndTargetMbti(Long userId, Long placeId, MbtiContext targetMbti);

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

    @Query("""
        select distinct pr.targetMbti
        from PlaceReaction pr
        join pr.place p
        where pr.user.id = :userId
          and pr.place.id = :placeId
          and pr.type = :type
          and p.deleted = false
    """)
    Set<MbtiContext> findContextsByUserIdAndPlaceIdAndType(Long userId, Long placeId, ActionType type);

    void deleteByPlaceId(Long placeId);
}

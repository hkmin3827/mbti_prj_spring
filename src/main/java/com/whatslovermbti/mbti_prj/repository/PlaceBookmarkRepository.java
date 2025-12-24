package com.whatslovermbti.mbti_prj.repository;

import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.entity.PlaceBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PlaceBookmarkRepository extends JpaRepository<PlaceBookmark, Long> {
    boolean existsByUserIdAndPlaceIdAndTargetMbti(Long userId, Long placeId, MbtiContext targetMbti);

    Optional<PlaceBookmark> findByUserIdAndPlaceIdAndTargetMbti(Long userId, Long placeId, MbtiContext targetMbti);

    // 내가 저장한 장소들 (최신순)
    @Query("""
        select pb
            from PlaceBookmark pb
            join pb.place p
            where pb.user.id = :userId
              and p.deleted = false
              and pb.createdAt = (
                  select max(pb2.createdAt)
                  from PlaceBookmark pb2
                  join pb2.place p2
                  where pb2.user.id = :userId
                    and pb2.place.id = pb.place.id
                    and p2.deleted = false
        )
        order by pb.createdAt desc
    """)
    List<PlaceBookmark> findLatestBookmarksPerPlace(Long userId);

    @Query("""
        select distinct pb.targetMbti
        from PlaceBookmark pb
        join pb.place p
        where pb.user.id = :userId
          and pb.place.id = :placeId
          and p.deleted = false
    """)
    Set<MbtiContext> findContextsByUserIdAndPlaceId(Long userId, Long placeId);

    void deleteByUserIdAndPlaceIdAndTargetMbti(
            Long userId,
            Long placeId,
            MbtiContext targetMbti
    );

    void deleteByPlaceId(Long placeId);
}

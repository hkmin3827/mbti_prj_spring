package com.whatslovermbti.mbti_prj.repository;

import com.whatslovermbti.mbti_prj.dto.place.PlaceReviewCountDto;
import com.whatslovermbti.mbti_prj.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 후기 게시판: 전체 리뷰 (삭제된 place 포함) - 최신순
    @EntityGraph(attributePaths = {"user", "place"})
    Page<Review> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 내 리뷰: 특정 유저 리뷰 (삭제된 place 포함) - 최신순
    @EntityGraph(attributePaths = {"user", "place"})
        @Query("""
        select r
        from Review r
        join r.place p
        where r.user.id = :userId
          and p.deleted = false
        order by r.createdAt desc
    """)
    Page<Review> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    // 영수증 중복 사용 여부
    boolean existsByUserIdAndReceiptHash(Long userId, String receiptHash);

    void deleteByPlaceId(Long placeId);

    @EntityGraph(attributePaths = {"user", "place"})
    Page<Review> findByPlaceIdOrderByCreatedAtDesc(Long placeId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "place"})
    Page<Review> findByPlace_NameContainingIgnoreCaseOrderByCreatedAtDesc(
            String placeName, Pageable pageable);

    @Query("""
    select new com.whatslovermbti.mbti_prj.dto.place.PlaceReviewCountDto(
        p.id,
        p.name,
        p.category,
        p.address,
        p.roadAddress,
        p.rating,
        p.kakaoPlaceId,
        p.telnum,
        count(r)
    )
    from Review r
    join r.place p
    where p.deleted = false
    group by
        p.id,
        p.name,
        p.category,
        p.address,
        p.roadAddress,
        p.rating,
        p.kakaoPlaceId,
        p.telnum
    order by count(r) desc
""")
    List<PlaceReviewCountDto> findMostReviewedPlaces(Pageable pageable);

    @Query("""
        select avg(r.rating)
        from Review r
        where r.place.id = :placeId
    """)
    Double findAverageRatingByPlaceId(Long placeId);
}

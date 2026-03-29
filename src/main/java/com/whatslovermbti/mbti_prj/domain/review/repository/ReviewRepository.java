package com.whatslovermbti.mbti_prj.domain.review.repository;

import com.whatslovermbti.mbti_prj.domain.review.dto.PlaceReviewCountDto;
import com.whatslovermbti.mbti_prj.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @EntityGraph(attributePaths = {"user", "place"})
    Page<Review> findAllByOrderByCreatedAtDesc(Pageable pageable);

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

    boolean existsByUserIdAndReceiptHash(Long userId, String receiptHash);

    void deleteByPlaceId(Long placeId);

    @EntityGraph(attributePaths = {"user", "place"})
    Page<Review> findByPlaceIdOrderByCreatedAtDesc(Long placeId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "place"})
    Page<Review> findByPlace_NameContainingIgnoreCaseOrderByCreatedAtDesc(
            String placeName, Pageable pageable);

    @Query("""
    select new com.whatslovermbti.mbti_prj.domain.review.dto.PlaceReviewCountDto(
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

    @Modifying
    @Query("DELETE FROM Review r WHERE r.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}

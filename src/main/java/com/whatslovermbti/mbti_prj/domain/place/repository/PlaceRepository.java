package com.whatslovermbti.mbti_prj.domain.place.repository;

import com.whatslovermbti.mbti_prj.global.constant.Category;
import com.whatslovermbti.mbti_prj.domain.place.entity.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByKakaoPlaceId(String kakaoPlaceId);

    List<Place> findAllByDeletedFalse();

    @Query("""
        select p
        from Place p
        where
        (:token1 is null or p.name like concat('%', :token1, '%'))
        and
        (:token2 is null or p.name like concat('%', :token2, '%'))
    """)
    List<Place> searchByNameTokens(
            @Param("token1") String token1,
            @Param("token2") String token2
    );

    Page<Place> findByNameContainingIgnoreCase(
            String name, Pageable pageable);

    Page<Place> findByCategory(
            Category category, Pageable pageable);

    Page<Place> findByCategoryAndNameContainingIgnoreCase(
            Category category, String name, Pageable pageable);
}

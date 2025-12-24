package com.whatslovermbti.mbti_prj.repository;

import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // ✅ 후기 게시판: 전체 리뷰 (삭제된 place 포함) - 최신순
    Page<Review> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // ✅ 내 리뷰: 특정 유저 리뷰 (삭제된 place 포함) - 최신순
    Page<Review> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

}

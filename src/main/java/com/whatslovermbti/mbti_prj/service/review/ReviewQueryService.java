package com.whatslovermbti.mbti_prj.service.review;

import com.whatslovermbti.mbti_prj.entity.Review;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewQueryService {
    private final ReviewRepository reviewRepository;

    // 후기게시판 : 전체 리뷰 조회(최신순)
    public Page<Review> getReviewBoard(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reviewRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    // 내 리뷰 조회 : soft delete 된 place도 포함
    public Page<Review> getMyReviews(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reviewRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable);
    }
}

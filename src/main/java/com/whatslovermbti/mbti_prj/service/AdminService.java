package com.whatslovermbti.mbti_prj.service;

import com.whatslovermbti.mbti_prj.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.entity.Review;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.exception.CustomException;
import com.whatslovermbti.mbti_prj.repository.ReviewRepository;
import com.whatslovermbti.mbti_prj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<User> getActiveUsers(Pageable pageable) {
        return userRepository.findByIsActive(true, pageable);
    }

    @Transactional(readOnly = true)
    public Page<User> getInactiveUsers(Pageable pageable) {
        return userRepository.findByIsActive(false, pageable);
    }

    public void activateUser(Long userId){
        User user = getUser(userId);
        if(user.isActive()){
            throw new CustomException(ErrorCode.USER_ALREADY_ACTIVE);
        }
        user.activate();
    }

    public void deactivateUser(Long userId) {
        User user = getUser(userId);

        if (!user.isActive()) {
            throw new CustomException(ErrorCode.USER_ALREADY_INACTIVE);
        }
        user.deactivate();
    }


    public Page<Review> getAllReviewsLatest(Pageable pageable) {
        return reviewRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        reviewRepository.delete(review);
    }
}

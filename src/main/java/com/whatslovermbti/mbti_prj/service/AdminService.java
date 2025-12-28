package com.whatslovermbti.mbti_prj.service;

import com.whatslovermbti.mbti_prj.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.Review;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.exception.CustomException;
import com.whatslovermbti.mbti_prj.repository.*;
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
    private final PlaceRepository placeRepository;
    private final PlaceBookmarkRepository placeBookmarkRepository;
    private final PlaceViewHistoryRepository placeViewHistoryRepository;
    private final PlaceReactionRepository placeReactionRepository;
    private final PlaceKeywordRepository placeKeywordRepository;
    private final UserActionLogRepository userActionLogRepository;

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

    public void activateUser(Long userId) {
        User user = getUser(userId);
        if (user.isActive()) {
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

    @Transactional
    public void softDeletePlace(Long placeId) {
        Place place = placeRepository.findById(placeId).orElseThrow(
                ()-> new CustomException(ErrorCode.PLACE_NOT_FOUND));
        place.setDeleted(true);
    }

    @Transactional
    public void hardDeletePlace(Long placeId) {

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));

        // 연관 리뷰 삭제
        reviewRepository.deleteByPlaceId(place.getId());

        userActionLogRepository.deleteByPlaceId(placeId);

        // 행동 데이터 삭제
        placeBookmarkRepository.deleteByPlaceId(place.getId());
        placeViewHistoryRepository.deleteByPlaceId(place.getId());
        placeReactionRepository.deleteByPlaceId(place.getId());

        // 연관 키워드 연결 삭제 (PlaceKeyword)
        placeKeywordRepository.deleteByPlaceId(place.getId());

        // Place 자체 삭제
        placeRepository.delete(place);
    }
}

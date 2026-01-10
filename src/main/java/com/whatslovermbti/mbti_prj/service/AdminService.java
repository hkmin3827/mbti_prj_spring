package com.whatslovermbti.mbti_prj.service;

import com.whatslovermbti.mbti_prj.constant.Category;
import com.whatslovermbti.mbti_prj.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.constant.Role;
import com.whatslovermbti.mbti_prj.dto.review.ReviewResponse;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.Review;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.exception.CustomException;
import com.whatslovermbti.mbti_prj.repository.*;
import com.whatslovermbti.mbti_prj.service.place.PlaceRatingService;
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
    private final PlaceRatingService placeRatingService;

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }


    @Transactional(readOnly = true)
    public Page<User> getUsers(String keyword, Boolean active, Pageable pageable) {

        boolean hasKeyword = keyword != null && keyword.trim().length() >= 2;
        Role targetRole = Role.USER;

        if (!hasKeyword && active == null) {
            return userRepository.findByRole(targetRole, pageable); // 기본 전체 조회
        }

        if (hasKeyword && active == null) {
            return userRepository.findByRoleAndNameContainingIgnoreCase(
                    targetRole, keyword, pageable);
        }

        if (!hasKeyword) {
            return userRepository.findByRoleAndIsActive(
                    targetRole, active, pageable
            );
        }

        return userRepository.findByRoleAndIsActiveAndNameContainingIgnoreCase(
                targetRole, active, keyword, pageable
        );
    }

    @Transactional(readOnly = true)
    public Page<User> getActiveUsers(Pageable pageable) {
        return userRepository.findByRoleAndIsActive(
                Role.USER, true, pageable
        );
    }

    @Transactional(readOnly = true)
    public Page<User> getInactiveUsers(Pageable pageable) {
        return userRepository.findByRoleAndIsActive(
                Role.USER, false, pageable
        );
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


    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviews(String placeName, Pageable pageable) {

        boolean hasPlaceName =
                placeName != null && placeName.trim().length() >= 2;

        Page<Review> page;

        // 기본: 전체 리뷰
        if (!hasPlaceName) {
            page = reviewRepository.findAllByOrderByCreatedAtDesc(pageable);
        } else {
            // Place 이름으로 필터
            page = reviewRepository
                    .findByPlace_NameContainingIgnoreCaseOrderByCreatedAtDesc(
                            placeName, pageable
                    );
        }

        // ✅ 엔티티 -> DTO 변환 (LazyProxy 직렬화 문제 차단)
        return page.map(ReviewResponse::from);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        reviewRepository.delete(review);

        placeRatingService.recalcPlaceRating(review.getPlace().getId());
    }

    @Transactional(readOnly = true)
    public Page<Place> getPlaces(
            String keyword,
            Category category,
            Pageable pageable
    ) {

        boolean hasKeyword = keyword != null && keyword.trim().length() >= 2;

        // 전체 조회 (기본)
        if (!hasKeyword && category == null) {
            return placeRepository.findAll(pageable);
        }

        // 카테고리만 필터
        if (!hasKeyword) {
            return placeRepository.findByCategory(category, pageable);
        }

        // 검색만
        if (category == null) {
            return placeRepository.findByNameContainingIgnoreCase(
                    keyword, pageable);
        }

        // 검색 + 카테고리
        return placeRepository.findByCategoryAndNameContainingIgnoreCase(
                category, keyword, pageable);
    }

    @Transactional
    public void softDeletePlace(Long placeId) {
        Place place = placeRepository.findById(placeId).orElseThrow(
                ()-> new CustomException(ErrorCode.PLACE_NOT_FOUND));
        place.setDeleted(true);
    }

    public void restorePlace(Long placeId){
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));

        if (!place.isDeleted()) {
            return; // 이미 활성 상태
        }

        place.setDeleted(false);
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

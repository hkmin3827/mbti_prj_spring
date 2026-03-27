package com.whatslovermbti.mbti_prj.application.admin;

import com.whatslovermbti.mbti_prj.global.constant.Category;
import com.whatslovermbti.mbti_prj.global.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.global.constant.Role;
import com.whatslovermbti.mbti_prj.domain.action.bookmark.repository.PlaceBookmarkRepository;
import com.whatslovermbti.mbti_prj.domain.action.reaction.repository.PlaceReactionRepository;
import com.whatslovermbti.mbti_prj.domain.action.view.repository.PlaceViewHistoryRepository;
import com.whatslovermbti.mbti_prj.domain.actionLog.repository.UserActionLogRepository;
import com.whatslovermbti.mbti_prj.domain.place.repository.PlaceRepository;
import com.whatslovermbti.mbti_prj.domain.placeKeyword.repository.PlaceKeywordRepository;
import com.whatslovermbti.mbti_prj.domain.review.repository.ReviewRepository;
import com.whatslovermbti.mbti_prj.domain.user.repository.UserRepository;
import com.whatslovermbti.mbti_prj.domain.review.dto.ReviewResponse;
import com.whatslovermbti.mbti_prj.domain.place.entity.Place;
import com.whatslovermbti.mbti_prj.domain.review.entity.Review;
import com.whatslovermbti.mbti_prj.domain.user.entity.User;
import com.whatslovermbti.mbti_prj.global.exception.CustomException;
import com.whatslovermbti.mbti_prj.application.place.PlaceRatingService;
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
            return userRepository.findByRole(targetRole, pageable);
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

        if (!hasPlaceName) {
            page = reviewRepository.findAllByOrderByCreatedAtDesc(pageable);
        } else {
            page = reviewRepository
                    .findByPlace_NameContainingIgnoreCaseOrderByCreatedAtDesc(
                            placeName, pageable
                    );
        }

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

        if (!hasKeyword && category == null) {
            return placeRepository.findAll(pageable);
        }

        if (!hasKeyword) {
            return placeRepository.findByCategory(category, pageable);
        }

        if (category == null) {
            return placeRepository.findByNameContainingIgnoreCase(
                    keyword, pageable);
        }

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
            return;
        }

        place.setDeleted(false);
    }
    @Transactional
    public void hardDeletePlace(Long placeId) {

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));

        reviewRepository.deleteByPlaceId(place.getId());

        userActionLogRepository.deleteByPlaceId(placeId);

        placeBookmarkRepository.deleteByPlaceId(place.getId());
        placeViewHistoryRepository.deleteByPlaceId(place.getId());
        placeReactionRepository.deleteByPlaceId(place.getId());

        placeKeywordRepository.deleteByPlaceId(place.getId());

        placeRepository.delete(place);
    }


}

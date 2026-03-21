package com.whatslovermbti.mbti_prj.service.review;

import com.whatslovermbti.mbti_prj.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.constant.ReviewSortType;
import com.whatslovermbti.mbti_prj.dto.place.PlaceDetailResponse;
import com.whatslovermbti.mbti_prj.dto.review.ReviewResponse;
import com.whatslovermbti.mbti_prj.entity.Review;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.exception.CustomException;
import com.whatslovermbti.mbti_prj.repository.ReviewRepository;
import com.whatslovermbti.mbti_prj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewQueryService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public Page<ReviewResponse> getReviewBoard(
            int page,
            int size,
            ReviewSortType sort
    ) {
        Sort sortCondition =
                sort == ReviewSortType.VIEWS
                        ? Sort.by(Sort.Direction.DESC, "viewCount")
                        : Sort.by(Sort.Direction.DESC, "createdAt");

        Pageable pageable = PageRequest.of(page, size, sortCondition);

        return reviewRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(review -> ReviewResponse.from(review));
    }

    public Page<ReviewResponse> getReviewsByPlace(
            Long placeId,
            int page,
            int size
    ) {
        return reviewRepository
                .findByPlaceIdOrderByCreatedAtDesc(
                        placeId,
                        PageRequest.of(page, size)
                )
                .map(ReviewResponse::from);
    }

    public Page<ReviewResponse> getMyReviews(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reviewRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable).map(review -> ReviewResponse.from(review));
    }

    public ReviewResponse getReviewDetail(Long reviewId, Long userId){
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        User user = userRepository.findById(userId).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        return  ReviewResponse.from(review, user);
    }

    public List<PlaceDetailResponse> getMostReviewedPlaces(int limit) {
        return reviewRepository
                .findMostReviewedPlaces(PageRequest.of(0, limit))
                .stream()
                .map(dto -> PlaceDetailResponse.builder()
                        .id(dto.placeId())
                        .name(dto.name())
                        .category(dto.category())
                        .address(dto.address())
                        .roadAddress(dto.roadAddress())
                        .rating(dto.rating())
                        .kakaoPlaceId(dto.kakaoPlaceId())
                        .telnum(dto.telnum())
                        .build()
                )
                .toList();
    }
}

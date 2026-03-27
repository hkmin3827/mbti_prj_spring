package com.whatslovermbti.mbti_prj.interfaces;

import com.whatslovermbti.mbti_prj.global.constant.ReviewSortType;
import com.whatslovermbti.mbti_prj.domain.place.dto.PlaceDetailResponse;
import com.whatslovermbti.mbti_prj.domain.review.dto.PlaceSearchResDto;
import com.whatslovermbti.mbti_prj.domain.review.dto.ReviewResponse;
import com.whatslovermbti.mbti_prj.domain.review.dto.ReviewUpdateReqDto;
import com.whatslovermbti.mbti_prj.domain.review.entity.Review;
import com.whatslovermbti.mbti_prj.domain.user.entity.User;
import com.whatslovermbti.mbti_prj.global.security.auth.CustomUserDetails;
import com.whatslovermbti.mbti_prj.application.place.PlaceQueryService;
import com.whatslovermbti.mbti_prj.application.review.ReviewQueryService;
import com.whatslovermbti.mbti_prj.application.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewQueryService reviewQueryService;
    private final ReviewService reviewService;
    private final PlaceQueryService placeQueryService;

    @PatchMapping("/{reviewId}/view")
    public ResponseEntity<Void> increaseViewCount(
            @PathVariable Long reviewId
    ) {
        reviewService.increaseViewCount(reviewId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search-place")
    public List<PlaceSearchResDto> searchPlace(
            @RequestParam String keyword
    ) {
        return placeQueryService.searchForReview(keyword)
                .stream()
                .map(PlaceSearchResDto::from)
                .toList();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createReview(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long placeId,
            @RequestParam int rating,
            @RequestParam String content,
            @RequestParam(required = false) String reviewImageUrl,
            @RequestPart(required = true) MultipartFile receiptImage
    ) {
        User user = userDetails.getUser();
        Review review = reviewService.createReview(
                user,
                placeId,
                rating,
                content,
                reviewImageUrl,
                receiptImage
        );

        return ResponseEntity.ok(review.getId());
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getReviewDetail(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        User user = userDetails.getUser();
        ReviewResponse result = reviewQueryService.getReviewDetail(reviewId, user.getId());

        return ResponseEntity.ok(result);
    }


    @PatchMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateReqDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        User user = userDetails.getUser();
        reviewService.updateReview(
                reviewId,
                user,
                dto.getRating(),
                dto.getContent(),
                dto.getReviewImageUrl(),
                dto.isRemoveImage()
        );
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        User user = userDetails.getUser();
        reviewService.deleteReview(reviewId, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public Page<ReviewResponse> reviewBoard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "LATEST") ReviewSortType sort
    ) {
        return reviewQueryService.getReviewBoard(page, size, sort);
    }

    @GetMapping("/me")
    public Page<ReviewResponse> myReviews(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        User user = userDetails.getUser();
        return reviewQueryService.getMyReviews(
                user,
                page,
                size
        );
    }


    @GetMapping("/most-reviewed")
    public List<PlaceDetailResponse> getMostReviewedPlaces(
            @RequestParam(defaultValue = "5") int limit
    ) {
        return reviewQueryService.getMostReviewedPlaces(limit);
    }
}

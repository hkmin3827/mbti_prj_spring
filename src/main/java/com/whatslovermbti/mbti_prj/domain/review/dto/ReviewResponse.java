package com.whatslovermbti.mbti_prj.domain.review.dto;

import com.whatslovermbti.mbti_prj.domain.review.entity.Review;
import com.whatslovermbti.mbti_prj.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewResponse {

    private Long id;
    private int rating;
    private String content;
    private String reviewImageUrl;
    private long viewCount;
    private boolean verified;
    private LocalDateTime createdAt;

    private UserInfo user;
    private PlaceInfo place;
    private Boolean mine;

    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getRating(),
                review.getContent(),
                review.getReviewImageUrl(),
                review.getViewCount(),
                review.isVerified(),
                review.getCreatedAt(),
                new UserInfo(review.getUser().getId(),review.getUser().getName(),review.getUser().getProfileImage(), review.getUser().getMbti(), review.getUser().getPartnerMbti()),
                new PlaceInfo(review.getPlace().getId(), review.getPlace().getName(), review.getPlace().getAddress()),
                null
        );
    }

    @Getter
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String name;
        private String profileImg;
        private String mbti;
        private String partnerMbti;
    }

    @Getter
    @AllArgsConstructor
    public static class PlaceInfo {
        private Long id;
        private String name;
        private String address;
    }


    // 리뷰 detail 페이지용
    public static ReviewResponse from(Review review, User viewer) {
        return new ReviewResponse(
                review.getId(),
                review.getRating(),
                review.getContent(),
                review.getReviewImageUrl(),
                review.getViewCount(),
                review.isVerified(),
                review.getCreatedAt(),
                new UserInfo(
                        review.getUser().getId(),
                        review.getUser().getName(),
                        review.getUser().getProfileImage(),
                        review.getUser().getMbti(),
                        review.getUser().getPartnerMbti()
                ),
                new PlaceInfo(
                        review.getPlace().getId(),
                        review.getPlace().getName(),
                        review.getPlace().getAddress()
                ),
                review.getUser().getId().equals(viewer.getId())
        );
    }
}

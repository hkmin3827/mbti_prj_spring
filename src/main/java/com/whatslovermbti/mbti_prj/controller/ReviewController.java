package com.whatslovermbti.mbti_prj.controller;

import com.whatslovermbti.mbti_prj.entity.Review;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.security.auth.CustomUserDetails;
import com.whatslovermbti.mbti_prj.service.CurrentUserService;
import com.whatslovermbti.mbti_prj.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final CurrentUserService currentUserService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createReview(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long placeId,
            @RequestParam int rating,
            @RequestParam String content,
            // 클라이언트가 S3에 직접 업로드한 URL
            @RequestParam(required = false) String reviewImageUrl,

            // OCR용 영수증만 서버로
            @RequestPart(required = false) MultipartFile receiptImage
    ) {
        User user = currentUserService.getCurrentUser(userDetails);
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
}

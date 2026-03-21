package com.whatslovermbti.mbti_prj.service.review;

import com.whatslovermbti.mbti_prj.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.Review;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.exception.CustomException;
import com.whatslovermbti.mbti_prj.receipt.model.ReceiptInfo;
import com.whatslovermbti.mbti_prj.receipt.model.ScoreVO;
import com.whatslovermbti.mbti_prj.repository.PlaceRepository;
import com.whatslovermbti.mbti_prj.repository.ReviewRepository;
import com.whatslovermbti.mbti_prj.service.ocr.GoogleVisionOcrService;
import com.whatslovermbti.mbti_prj.service.ocr.ReceiptVerificationService;
import com.whatslovermbti.mbti_prj.service.place.PlaceRatingService;
import com.whatslovermbti.mbti_prj.service.s3.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final GoogleVisionOcrService ocrService;
    private final PlaceRepository placeRepository;
    private final ReceiptVerificationService receiptVerificationService;
    private final S3Service s3Service;
    private final PlaceMatcher placeMatcher;
    private final PlaceRatingService placeRatingService;

    public void increaseViewCount(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다."));

        review.setViewCount(review.getViewCount() + 1);
    }
    public Review createReview(
            User user,
            Long placeId,
            int rating,
            String content,
            String reviewImageUrl,
            MultipartFile receiptImage
    ) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));

        Review review = new Review();
        review.setUser(user);
        review.setPlace(place);
        review.setRating(rating);
        review.setContent(content);

        if (reviewImageUrl != null && !reviewImageUrl.isBlank()) {
            review.setReviewImageUrl(reviewImageUrl);
        }

        if (receiptImage == null || receiptImage.isEmpty()) {
            review.setVerified(false);
            return reviewRepository.save(review);
        }

        String ocrText = ocrService.extractText(receiptImage);

        List<String> lines = Arrays.stream(ocrText.split("\\r?\\n"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        ScoreVO result =
                receiptVerificationService.verify(lines);

        String receiptHash = generateReceiptHash(result.getReceipt());
        boolean alreadyUsed =
                reviewRepository.existsByUserIdAndReceiptHash(
                        user.getId(),
                        receiptHash
                );
        if (alreadyUsed) {
            throw new CustomException(ErrorCode.DUPLICATE_RECEIPT_REVIEW);
        }

        int matchScore = placeMatcher.match(place, result.getReceipt());
        review.setPlaceMatchScore(matchScore);

        review.setVerified(result.isVerified());

        String receiptUrl = s3Service.uploadFile(receiptImage, "receipts");
        review.setReceiptImageUrl(receiptUrl);

        Review saved = reviewRepository.save(review);

        placeRatingService.recalcPlaceRating(placeId);

        return saved;
    }


    private String generateReceiptHash(ReceiptInfo receipt) {

        String raw =
                receipt.getStoreName() + "|" +
                        receipt.getDate() + "|" +
                        receipt.getTotalAmount();

        return DigestUtils.sha256Hex(raw);
    }

    public void updateReview(
            Long reviewId,
            User user,
            int rating,
            String content,
            String reviewImageUrl,
            boolean removeImage
    ) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        review.setRating(rating);
        review.setContent(content);

        if(reviewImageUrl != null){
            review.setReviewImageUrl(reviewImageUrl);
        }
        else if (removeImage) {
            review.setReviewImageUrl(null);
        }

        placeRatingService.recalcPlaceRating(review.getPlace().getId());
    }

    public void deleteReview(
            Long reviewId,
            User user
    ) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        reviewRepository.delete(review);

        placeRatingService.recalcPlaceRating(review.getPlace().getId());
    }
}
package com.whatslovermbti.mbti_prj.service;

import com.whatslovermbti.mbti_prj.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.Review;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.exception.CustomException;
import com.whatslovermbti.mbti_prj.receipt.model.ScoreVO;
import com.whatslovermbti.mbti_prj.repository.PlaceRepository;
import com.whatslovermbti.mbti_prj.repository.ReviewRepository;
import com.whatslovermbti.mbti_prj.service.ocr.GoogleVisionOcrService;
import com.whatslovermbti.mbti_prj.service.ocr.ReceiptVerificationService;
import com.whatslovermbti.mbti_prj.service.s3.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final GoogleVisionOcrService ocrService;
    private final PlaceRepository placeRepository;
    private final ReceiptVerificationService receiptVerificationService;
    private final S3Service s3Service;

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

        // 영수증 업로드 안 한 경우
        if (receiptImage == null || receiptImage.isEmpty()) {
            review.setVerified(false);
            return reviewRepository.save(review);
        }

        // OCR 수행
        String ocrText = ocrService.extractText(receiptImage);

        // OCR 결과를 Line 단위로 분리
        List<String> lines = Arrays.stream(ocrText.split("\\r?\\n"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        // 영수증 파싱 + 신뢰도 계산
        ScoreVO result =
                receiptVerificationService.verify(lines);


//        이후
//        placeMatcher.match(place, result.getReceipt());


        // 신뢰도 기준으로 인증 여부 결정
        review.setVerified(result.isVerified());

        // 영수증은 서버에서 S3 업로드 (private)
        String receiptUrl = s3Service.uploadFile(receiptImage, "receipts");
        review.setReceiptImageUrl(receiptUrl);

        return reviewRepository.save(review);
    }
}
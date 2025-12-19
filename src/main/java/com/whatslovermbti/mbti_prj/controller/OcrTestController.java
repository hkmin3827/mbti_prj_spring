package com.whatslovermbti.mbti_prj.controller;

import com.whatslovermbti.mbti_prj.receipt.model.ScoreVO;
import com.whatslovermbti.mbti_prj.service.ocr.GoogleVisionOcrService;
import com.whatslovermbti.mbti_prj.service.ocr.ReceiptVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test/ocr")
@Slf4j
public class OcrTestController {

    private final GoogleVisionOcrService googleVisionOcrService;
    private final ReceiptVerificationService receiptVerificationService;

    /**
     * OCR 연결 테스트용
     * - 이미지 업로드
     * - OCR 결과 반환
     * - 파싱된 영수증 정보 반환
     */
    @PostMapping("/receipt")
    public ResponseEntity<ScoreVO> testReceiptOcr(
            @RequestParam("file") MultipartFile file
    ) {
        // 1️⃣ OCR 호출
        String ocrText = googleVisionOcrService.extractText(file);
        log.info("OCR RAW TEXT:\n{}", ocrText);

        // 2️⃣ OCR 결과 → Line 단위 분리
        List<String> lines = Arrays.stream(ocrText.split("\\r?\\n"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        // 3️⃣ 영수증 검증 (파싱 + 스코어링)
        ScoreVO result =
                receiptVerificationService.verify(lines);

        log.info("RECEIPT RESULT: score={}, verified={}",
                result.getScore(), result.isVerified());

        // 4️⃣ 결과 반환
        return ResponseEntity.ok(result);
    }
}


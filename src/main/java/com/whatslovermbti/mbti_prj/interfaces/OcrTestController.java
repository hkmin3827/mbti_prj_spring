package com.whatslovermbti.mbti_prj.interfaces;

import com.whatslovermbti.mbti_prj.infra.receipt.model.ScoreVO;
import com.whatslovermbti.mbti_prj.application.ocr.GoogleVisionOcrService;
import com.whatslovermbti.mbti_prj.application.ocr.ReceiptVerificationService;
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


    @PostMapping("/receipt")
    public ResponseEntity<ScoreVO> testReceiptOcr(
            @RequestParam("file") MultipartFile file
    ) {
        String ocrText = googleVisionOcrService.extractText(file);
        log.info("OCR RAW TEXT:\n{}", ocrText);

        List<String> lines = Arrays.stream(ocrText.split("\\r?\\n"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        ScoreVO result =
                receiptVerificationService.verify(lines);

        log.info("RECEIPT RESULT: score={}, verified={}",
                result.getScore(), result.isVerified());

        return ResponseEntity.ok(result);
    }
}


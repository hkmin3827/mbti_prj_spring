package com.whatslovermbti.mbti_prj.interfaces;

import com.whatslovermbti.mbti_prj.application.ocr.GoogleVisionOcrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ocr")
public class OcrController {

    private final GoogleVisionOcrService googleVisionOcrService;

    @PostMapping("/receipt")
    public ResponseEntity<String> extractReceiptText(@RequestParam("file") MultipartFile file) {
        String text = googleVisionOcrService.extractText(file);
        return ResponseEntity.ok(text);
    }
}
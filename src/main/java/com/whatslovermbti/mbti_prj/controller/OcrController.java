package com.whatslovermbti.mbti_prj.controller;

//import com.whatslovermbti.mbti_prj.service.GoogleVisionOcrService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/ocr")
//public class OcrController {
//
//    private final GoogleVisionOcrService googleVisionOcrService;
//
//    /**
//     * 영수증 이미지 업로드 → OCR 텍스트 반환 테스트용
//     */
//    @PostMapping("/receipt")
//    public ResponseEntity<String> extractReceiptText(@RequestParam("file") MultipartFile file) {
//        String text = googleVisionOcrService.extractText(file);
//        return ResponseEntity.ok(text);
//    }
//}
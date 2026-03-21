package com.whatslovermbti.mbti_prj.controller;

import com.whatslovermbti.mbti_prj.service.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/upload-url")
    public ResponseEntity<S3Service.PresignedUpload> getUploadUrl(
            @RequestParam String folder,
            @RequestParam String originalFileName,
            @RequestParam(defaultValue = "image/jpeg") String contentType
    ) {
        return ResponseEntity.ok(
                s3Service.createPresignedUpload(
                        folder,
                        originalFileName,
                        contentType
                )
        );
    }
}

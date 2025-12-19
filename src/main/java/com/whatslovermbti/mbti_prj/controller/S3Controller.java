package com.whatslovermbti.mbti_prj.controller;

import com.whatslovermbti.mbti_prj.service.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/upload-url")
    public ResponseEntity<Map<String, String>> getUploadUrl(
            @RequestParam String folder,
            @RequestParam String fileName
    ) {
        String url = s3Service.createPresignedUploadUrl(folder, fileName);

        Map<String, String> response = new HashMap<>();
        response.put("uploadUrl", url);
        response.put("fileUrl", s3Service.getFileUrl(folder, fileName));

        return ResponseEntity.ok(response);
    }
}

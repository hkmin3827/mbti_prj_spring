package com.whatslovermbti.mbti_prj.service.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String createPresignedUploadUrl(String folder, String fileName) {

        String key = folder + "/" + fileName;

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest =
                presigner.presignPutObject(presignRequest);

        return presignedRequest.url().toString();
    }

    public String getFileUrl(String folder, String fileName) {
        return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/"
                + folder + "/" + fileName;
    }

    // presigned랑 별개로 서버에서 직접 올리는 방식 : 리뷰이미지 업로드 확인용. ocr에도 적용 가능
    public String uploadFile(MultipartFile file, String folder) {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String key = folder + "/" + fileName;

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    request,
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(
                            file.getInputStream(),
                            file.getSize()
                    )
            );

            return getFileUrl(folder, fileName);

        } catch (Exception e) {
            throw new RuntimeException("S3 업로드 실패", e);
        }
    }
}

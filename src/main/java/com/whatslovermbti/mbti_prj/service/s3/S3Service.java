package com.whatslovermbti.mbti_prj.service.s3;

import com.whatslovermbti.mbti_prj.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.exception.CustomException;
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
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final Set<String> ALLOWED_FOLDERS = Set.of(
            "review-images",
            "profile",
            "places",
            "receipts"
    );

    public PresignedUpload createPresignedUpload(
            String folder,
            String originalFileName,
            String contentType
    ) {
        validateFolder(folder);
        validateContentType(contentType);

        String extension = extractExtension(originalFileName);
        String key = folder + "/" + UUID.randomUUID() + extension;


        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        PutObjectPresignRequest presignRequest =
                PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .putObjectRequest(objectRequest)
                        .build();

        PresignedPutObjectRequest presigned =
                presigner.presignPutObject(presignRequest);

        return new PresignedUpload(
                presigned.url().toString(),
                getFileUrl(key)
        );
    }

    public String getFileUrl(String folder, String fileName) {
        return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/"
                + folder + "/" + fileName;
    }

    private void validateFolder(String folder) {
        if (!ALLOWED_FOLDERS.contains(folder)) {
            throw new CustomException(ErrorCode.INVALID_UPLOAD_FOLDER);
        }
    }
    private String extractExtension(String originalFileName) {
        int idx = originalFileName.lastIndexOf(".");
        if (idx == -1) {
            throw new CustomException(ErrorCode.INVALID_CONTENT_TYPE);
        }
        return originalFileName.substring(idx);
    }
    private void validateContentType(String contentType) {
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new CustomException(ErrorCode.INVALID_CONTENT_TYPE);
        }
    }


    private String getFileUrl(String key) {
        return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + key;
    }


    public record PresignedUpload(
            String uploadUrl,
            String fileUrl
    ) {}

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

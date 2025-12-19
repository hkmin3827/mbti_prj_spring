package com.whatslovermbti.mbti_prj.service.ocr;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GoogleVisionOcrService {

    /**
     * 업로드된 이미지 파일을 받아서 전체 텍스트를 반환
     */
    public String extractText(MultipartFile file) {
        try {
            byte[] imageBytes = file.getBytes();
            return detectText(imageBytes);
        } catch (IOException e) {
            log.error("이미지 파일 읽기 실패: {}", e.getMessage(), e);
            throw new RuntimeException("이미지 처리 중 오류가 발생했습니다.");
        }
    }

    /**
     * 실제 Google Vision API 호출 부분
     */
    private String detectText(byte[] imageBytes) {
        // GOOGLE_APPLICATION_CREDENTIALS 환경변수 설정 필요
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

            ByteString imgBytes = ByteString.copyFrom(imageBytes);

            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();

            AnnotateImageRequest request =
                    AnnotateImageRequest.newBuilder()
                            .addFeatures(feat)
                            .setImage(img)
                            .build();

            List<AnnotateImageRequest> requests = new ArrayList<>();
            requests.add(request);

            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            StringBuilder sb = new StringBuilder();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    log.error("Vision API 오류: {}", res.getError().getMessage());
                    throw new RuntimeException("Vision API 오류: " + res.getError().getMessage());
                }

                // 전체 텍스트(첫 번째 annotation)에 들어있음
                if (res.getTextAnnotationsCount() > 0) {
                    EntityAnnotation annotation = res.getTextAnnotations(0);
                    sb.append(annotation.getDescription());
                    // 여러 줄 포함된 전체 문자열 (개행 포함)
                }
            }
            log.info("구글 VISION API 호출 !!");

            return sb.toString();

        } catch (IOException e) {
            log.error("Google Vision API 호출 실패: {}", e.getMessage(), e);
            throw new RuntimeException("Vision API 호출 중 오류가 발생했습니다.");
        }
    }
}
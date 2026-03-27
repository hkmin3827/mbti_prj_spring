package com.whatslovermbti.mbti_prj.application.ocr;

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

    public String extractText(MultipartFile file) {
        try {
            byte[] imageBytes = file.getBytes();
            return detectText(imageBytes);
        } catch (IOException e) {
            log.error("이미지 파일 읽기 실패: {}", e.getMessage(), e);
            throw new RuntimeException("이미지 처리 중 오류가 발생했습니다.");
        }
    }

    private String detectText(byte[] imageBytes) {
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

                if (res.getTextAnnotationsCount() > 0) {
                    EntityAnnotation annotation = res.getTextAnnotations(0);
                    sb.append(annotation.getDescription());
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
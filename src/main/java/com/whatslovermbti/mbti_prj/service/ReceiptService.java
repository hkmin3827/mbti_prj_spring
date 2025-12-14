package com.whatslovermbti.mbti_prj.service;

import com.whatslovermbti.mbti_prj.entity.ReceiptInfo;
import org.springframework.stereotype.Service;

@Service
public class ReceiptService {
    public ReceiptInfo parseReceipt(String ocrText) {
        // 아주 단순한 예: 줄 단위 split 후 상호명, 주소 패턴 찾아내기
        String[] lines = ocrText.split("\\r?\\n");
        ReceiptInfo info = new ReceiptInfo();

        for (String line : lines) {
            if (info.getStoreName() == null && line.contains("카페")) {
                info.setStoreName(line.trim());
            }
            if (info.getAddress() == null && (line.contains("서울") || line.contains("경기"))) {
                info.setAddress(line.trim());
            }
            // 날짜/금액 패턴 정규식으로 찾기 등...
        }

        return info;
    }
}

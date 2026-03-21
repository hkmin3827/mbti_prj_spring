package com.whatslovermbti.mbti_prj.service.keyword;

import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.entity.KeywordNormalization;
import com.whatslovermbti.mbti_prj.repository.KeywordNormalizationRepository;
import com.whatslovermbti.mbti_prj.repository.KeywordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class KeywordNormalizationService {

    private final KeywordNormalizationRepository normalizationRepository;
    private final KeywordRepository keywordRepository;

    public Keyword normalize(String inputKeyword) {

        String normalized =
                normalizeKoreanForm(
                        inputKeyword.trim().toLowerCase()
                );

        return normalizationRepository
                .findByRawKeyword(normalized)
                .map(KeywordNormalization::getStandardKeyword)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "표준 키워드 매핑 실패: " + inputKeyword
                        )
                );
    }

    public String normalizeKoreanForm(String keyword) {
        return keyword.replaceAll("(적인|적|한)$", "").trim();
    }

    // 관리자/시드용: raw → standard 수동 매핑
    public void registerMapping(
            String rawKeyword,
            Long standardKeywordId
    ) {

        Keyword standard =
                keywordRepository.findById(standardKeywordId)
                        .orElseThrow(() ->
                                new IllegalArgumentException("존재하지 않는 표준 키워드")
                        );

        String normalizedRaw =
                normalizeKoreanForm(rawKeyword.trim().toLowerCase());

        normalizationRepository.save(
                new KeywordNormalization(normalizedRaw, standard)
        );
    }
}

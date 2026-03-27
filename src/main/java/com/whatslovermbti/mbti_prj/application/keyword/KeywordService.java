package com.whatslovermbti.mbti_prj.application.keyword;

import com.whatslovermbti.mbti_prj.global.constant.MbtiAxis;
import com.whatslovermbti.mbti_prj.domain.keyword.entity.Keyword;
import com.whatslovermbti.mbti_prj.domain.keyword.entity.KeywordNormalization;
import com.whatslovermbti.mbti_prj.domain.keyword.repository.KeywordNormalizationRepository;
import com.whatslovermbti.mbti_prj.domain.keyword.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final KeywordNormalizationRepository keywordNormalizationRepository;
    private final KeywordNormalizationService keywordNormalizationService;

    /**
     * 단독 표준 키워드 단건 생성 (관리자/시드용)
     */
    public Keyword createKeyword(String standardName, MbtiAxis axis) {

        String standard =
                standardName.trim().toLowerCase();

        if (keywordRepository.existsByName(standard)) {
            throw new IllegalArgumentException("이미 존재하는 키워드입니다.");
        }

        return keywordRepository.save(
                new Keyword(standard, axis)
        );
    }

    public Keyword createKeywordWithNormalizations(
            String standardName,
            MbtiAxis axis,
            List<String> variants
    ) {

        String standard =
                standardName.trim().toLowerCase();

        if (keywordRepository.existsByName(standard)) {
            throw new IllegalArgumentException("이미 존재하는 표준 키워드입니다.");
        }

        Keyword keyword =
                keywordRepository.save(
                        new Keyword(standard, axis)
                );

        for (String variant : variants) {
            String raw =
                    variant.trim().toLowerCase();

            keywordNormalizationRepository.save(
                    new KeywordNormalization(raw, keyword)
            );
        }

        return keyword;
    }

    @Transactional(readOnly = true)
    public List<Keyword> getAllKeywords() {
        return keywordRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Keyword getByName(String name) {
        return keywordRepository.findByName(name.trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 키워드입니다."));
    }

    /**
     * 장소 등록 시: 표준 키워드 조회
     */
    public Keyword getOrThrow(String name) {

        return keywordNormalizationService.normalize(name);
    }

}
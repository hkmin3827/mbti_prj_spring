package com.whatslovermbti.mbti_prj.service.keyword;

import com.whatslovermbti.mbti_prj.constant.MbtiAxis;
import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.entity.KeywordNormalization;
import com.whatslovermbti.mbti_prj.repository.KeywordNormalizationRepository;
import com.whatslovermbti.mbti_prj.repository.KeywordRepository;
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
     * 단독 표준 키워드 단건 생성 (변이 없음)
     * (관리자/시드용)
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

    // 표준 키워드 + 형태 변이 세트 생성
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

    /**
     * 키워드 전체 조회 (프론트 선택용)
     */
    @Transactional(readOnly = true)
    public List<Keyword> getAllKeywords() {
        return keywordRepository.findAll();
    }

    /**
     * 이름으로 키워드 조회 (없으면 예외)
     */
    @Transactional(readOnly = true)
    public Keyword getByName(String name) {
        return keywordRepository.findByName(name.trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 키워드입니다."));
    }

    /**
     * 장소 등록 시: 표준 키워드 조회
     * 없으면 예외 (자동 생성 ❌)
     */
    public Keyword getOrThrow(String name) {

        return keywordNormalizationService.normalize(name);
    }

}
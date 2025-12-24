package com.whatslovermbti.mbti_prj.service.keyword;

import com.whatslovermbti.mbti_prj.entity.Keyword;
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

    /**
     * 키워드 단건 생성 (관리자/시드용)
     */
    public Keyword createKeyword(String name) {

        String normalized = normalize(name);

        if (keywordRepository.existsByName(normalized)) {
            throw new IllegalArgumentException("이미 존재하는 키워드입니다.");
        }

        Keyword keyword = new Keyword();
        keyword.setName(normalized);

        return keywordRepository.save(keyword);
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
        return keywordRepository.findByName(normalize(name))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 키워드입니다."));
    }

    /**
     * 장소 등록 시: 키워드가 있으면 재사용, 없으면 생성
     * (※ 지금은 관리자/내부 로직에서만 사용)
     */
    public Keyword getOrCreate(String name) {

        String normalized = normalize(name);

        return keywordRepository.findByName(normalized)
                .orElseGet(() -> {
                    Keyword k = new Keyword();
                    k.setName(normalized);
                    return keywordRepository.save(k);
                });
    }

    /**
     * 키워드 정규화 (중요)
     */
    private String normalize(String name) {
        return name.trim().toLowerCase();
    }
}
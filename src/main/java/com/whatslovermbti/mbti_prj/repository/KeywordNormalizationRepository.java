package com.whatslovermbti.mbti_prj.repository;

import com.whatslovermbti.mbti_prj.entity.KeywordNormalization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeywordNormalizationRepository
        extends JpaRepository<KeywordNormalization, Long> {

    Optional<KeywordNormalization> findByRawKeyword(String rawKeyword);
}
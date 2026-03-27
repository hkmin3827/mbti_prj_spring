package com.whatslovermbti.mbti_prj.domain.keyword.repository;

import com.whatslovermbti.mbti_prj.domain.keyword.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findByName(String name);

    boolean existsByName(String name);

    List<Keyword> findByNameIn(List<String> names);
}

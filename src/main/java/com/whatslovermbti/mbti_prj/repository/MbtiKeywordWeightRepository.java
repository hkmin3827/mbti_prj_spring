package com.whatslovermbti.mbti_prj.repository;

import com.whatslovermbti.mbti_prj.entity.MbtiKeywordWeight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface MbtiKeywordWeightRepository extends JpaRepository<MbtiKeywordWeight, Long> {

    List<MbtiKeywordWeight> findByMbtiAxisIn(Collection<String> mbtiAxes);

    boolean existsByMbtiAxisAndKeyword_Id(String mbtiAxis, Long keywordId);
}
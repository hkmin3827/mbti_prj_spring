package com.whatslovermbti.mbti_prj.domain.keyword.repository;

import com.whatslovermbti.mbti_prj.global.constant.MbtiAxis;
import com.whatslovermbti.mbti_prj.domain.keyword.entity.MbtiKeywordWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface MbtiKeywordWeightRepository extends JpaRepository<MbtiKeywordWeight, Long> {
    @Query("""
        select w
        from MbtiKeywordWeight w
        join fetch w.keyword
        where w.mbtiAxis in :axes
    """)
    List<MbtiKeywordWeight> findAllByAxes(@Param("axes") Set<MbtiAxis> axes);
}
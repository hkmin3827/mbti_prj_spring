package com.whatslovermbti.mbti_prj.repository;

import com.whatslovermbti.mbti_prj.constant.MbtiAxis;
import com.whatslovermbti.mbti_prj.entity.MbtiKeywordWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface MbtiKeywordWeightRepository extends JpaRepository<MbtiKeywordWeight, Long> {

    List<MbtiKeywordWeight> findByMbtiAxisIn(Collection<String> mbtiAxes);

    boolean existsByMbtiAxisAndKeyword_Id(String mbtiAxis, Long keywordId);
    @Query("""
        select w
        from MbtiKeywordWeight w
        where w.mbtiAxis in :axes
    """)
    List<MbtiKeywordWeight> findAllByAxes(@Param("axes") Set<MbtiAxis> axes);
}
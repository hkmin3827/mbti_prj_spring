package com.whatslovermbti.mbti_prj.domain.placeKeyword.repository;

import com.whatslovermbti.mbti_prj.domain.place.entity.Place;
import com.whatslovermbti.mbti_prj.domain.placeKeyword.entity.PlaceKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceKeywordRepository extends JpaRepository<PlaceKeyword, Long> {
    List<PlaceKeyword> findByPlace(Place place);
    boolean existsByPlaceId(Long placeId);
    void deleteByPlaceId(Long placeId);
}

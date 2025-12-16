package com.whatslovermbti.mbti_prj.repository;

import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.PlaceBookmark;
import com.whatslovermbti.mbti_prj.entity.PlaceReaction;
import com.whatslovermbti.mbti_prj.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceBookmarkRepository extends JpaRepository<PlaceBookmark, Long> {
    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);

    Optional<PlaceBookmark> findByUserAndPlace(User user, Place place);
}

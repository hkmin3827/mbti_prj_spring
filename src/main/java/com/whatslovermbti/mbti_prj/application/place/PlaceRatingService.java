package com.whatslovermbti.mbti_prj.application.place;

import com.whatslovermbti.mbti_prj.domain.place.entity.Place;
import com.whatslovermbti.mbti_prj.domain.place.repository.PlaceRepository;
import com.whatslovermbti.mbti_prj.domain.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceRatingService {

    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    public void recalcPlaceRating(Long placeId) {
        Double avg = reviewRepository.findAverageRatingByPlaceId(placeId);

        Place place = placeRepository.findById(placeId)
                .orElseThrow();

        place.updateRating(avg != null ? avg : 0.0);
    }
}

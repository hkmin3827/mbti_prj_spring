package com.whatslovermbti.mbti_prj.service.place;

import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.repository.PlaceBookmarkRepository;
import com.whatslovermbti.mbti_prj.repository.PlaceReactionRepository;
import com.whatslovermbti.mbti_prj.repository.PlaceViewHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceBookmarkRepository placeBookmarkRepository;
    private final PlaceViewHistoryRepository placeViewHistoryRepository;
    private final PlaceReactionRepository placeReactionRepository;

    @Transactional
    public void softDeletePlace(Place place) {
        place.setDeleted(true);

        placeBookmarkRepository.deleteByPlaceId(place.getId());
        placeViewHistoryRepository.deleteByPlaceId(place.getId());
        placeReactionRepository.deleteByPlaceId(place.getId());
    }
}

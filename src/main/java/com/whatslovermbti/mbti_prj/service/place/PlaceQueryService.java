package com.whatslovermbti.mbti_prj.service.place;

import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceQueryService {

    private final PlaceRepository placeRepository;

    public List<Place> searchForReview(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        String[] tokens = keyword.trim().split("\\s+");

        String token1 = tokens.length > 0 ? tokens[0] : null;
        String token2 = tokens.length > 1 ? tokens[1] : null;

        return placeRepository.searchByNameTokens(token1, token2);
    }
}

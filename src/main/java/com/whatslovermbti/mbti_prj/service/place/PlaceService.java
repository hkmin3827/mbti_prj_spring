package com.whatslovermbti.mbti_prj.service.place;

import com.whatslovermbti.mbti_prj.constant.ActionType;
import com.whatslovermbti.mbti_prj.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.dto.place.PlaceDetailResponse;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.PlaceReaction;
import com.whatslovermbti.mbti_prj.exception.CustomException;
import com.whatslovermbti.mbti_prj.repository.PlaceReactionRepository;
import com.whatslovermbti.mbti_prj.repository.PlaceRepository;
import com.whatslovermbti.mbti_prj.service.action.UserActionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final PlaceReactionRepository placeReactionRepository;
    private final UserActionQueryService userActionQueryService;

    public PlaceDetailResponse getPlaceDetail(Long placeId, Long userId, MbtiContext context) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));

        if (place.isDeleted()) {
            throw new AccessDeniedException("해당 장소는 비활성화되어 접근할 수 없습니다.");
        }

        ActionType myReaction =
                placeReactionRepository
                        .findByUserIdAndPlaceIdAndTargetMbti(userId, placeId, context)
                        .map(PlaceReaction::getType)
                        .orElse(null);

        boolean bookmarked =
                userActionQueryService.isBookmarked(userId, placeId);


        return PlaceDetailResponse
                .fromPlace(place)
                .myReaction(myReaction)
                .bookmarked(bookmarked)
                .build();
    }
}

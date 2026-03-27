package com.whatslovermbti.mbti_prj.application.place;

import com.whatslovermbti.mbti_prj.global.constant.ActionType;
import com.whatslovermbti.mbti_prj.global.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.global.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.domain.place.dto.PlaceDetailResponse;
import com.whatslovermbti.mbti_prj.domain.place.entity.Place;
import com.whatslovermbti.mbti_prj.domain.action.reaction.entity.PlaceReaction;
import com.whatslovermbti.mbti_prj.global.exception.CustomException;
import com.whatslovermbti.mbti_prj.domain.action.reaction.repository.PlaceReactionRepository;
import com.whatslovermbti.mbti_prj.domain.place.repository.PlaceRepository;
import com.whatslovermbti.mbti_prj.application.action.UserActionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

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

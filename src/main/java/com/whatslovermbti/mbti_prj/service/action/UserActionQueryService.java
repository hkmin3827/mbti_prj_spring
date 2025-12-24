package com.whatslovermbti.mbti_prj.service.action;

import com.whatslovermbti.mbti_prj.constant.ActionType;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.dto.place.PlaceResDto;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.repository.PlaceBookmarkRepository;
import com.whatslovermbti.mbti_prj.repository.PlaceReactionRepository;
import com.whatslovermbti.mbti_prj.repository.PlaceViewHistoryRepository;
import com.whatslovermbti.mbti_prj.service.place.PlaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserActionQueryService {
    private final PlaceBookmarkRepository placeBookmarkRepository;
    private final PlaceMapper placeMapper;
    private final PlaceViewHistoryRepository placeViewHistoryRepository;
    private final PlaceReactionRepository placeReactionRepository;

    public List<PlaceResDto> getBookmarks(
            Long userId
    ) {
        return placeBookmarkRepository
                .findLatestBookmarksPerPlace(userId)
                .stream()
                .map(bookmark -> {
                    Place place = bookmark.getPlace();

                    PlaceResDto dto =
                            placeMapper.fromEntity(place, null);

                    // ✅ 여기서만 savedContexts 주입
                    Set<MbtiContext> contexts =
                            placeBookmarkRepository
                                    .findContextsByUserIdAndPlaceId(
                                            userId,
                                            place.getId()
                                    );

                    dto.setSavedContexts(contexts);
                    return dto;
                })
                .toList();
    }

    public List<PlaceResDto> getRecentViews(
            Long userId,
            PlaceMapper placeMapper
    ) {
        return placeViewHistoryRepository
                .findLatestViewsPerPlace(userId)
                .stream()
                .limit(20)
                .map(history -> placeMapper.fromEntity(history.getPlace(), null))
                .toList();
    }

    public List<PlaceResDto> getLikedPlaces(
            Long userId
    ) {

        return placeReactionRepository.findLatestReactionsPerPlace(userId, ActionType.LIKE)
                .stream()
                .map(reaction -> {
                    Place place = reaction.getPlace();

                    PlaceResDto dto = placeMapper.fromEntity(place, null);

                    Set<MbtiContext> contexts =
                            placeReactionRepository
                                    .findContextsByUserIdAndPlaceIdAndType(
                                            userId,
                                            place.getId(),
                                            ActionType.LIKE
                                    );
                    dto.setSavedContexts(contexts);
                    return dto;
                })
                .toList();
    }

}

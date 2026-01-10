package com.whatslovermbti.mbti_prj.service.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatslovermbti.mbti_prj.constant.ActionType;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.dto.place.PlaceBookmarkContextRow;
import com.whatslovermbti.mbti_prj.dto.place.PlaceLikedContextRow;
import com.whatslovermbti.mbti_prj.dto.place.PlaceResDto;
import com.whatslovermbti.mbti_prj.dto.place.PlaceViewCountDto;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.repository.*;
import com.whatslovermbti.mbti_prj.service.place.PlaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserActionQueryService {
    private final PlaceBookmarkRepository placeBookmarkRepository;
    private final PlaceMapper placeMapper;
    private final PlaceViewHistoryRepository placeViewHistoryRepository;
    private final PlaceReactionRepository placeReactionRepository;
    private final UserActionLogRepository userActionLogRepository;
    private final PlaceRepository placeRepository;

    public List<PlaceResDto> getBookmarks(
            Long userId
    ) {

        // 1최신순으로 정렬된 bookmark row 조회
        List<PlaceBookmarkContextRow> rows =
                placeBookmarkRepository.findBookmarkContextsByUser(userId);

        if (rows.isEmpty()) {
            return List.of();
        }

        // 최신순 placeId 리스트 (순서 보존)
        List<Long> orderedPlaceIds = rows.stream()
                .map(PlaceBookmarkContextRow::placeId)
                .toList();

        // placeId → bookmarkedContext 매핑
        Map<Long, MbtiContext> bookmarkContextMap = rows.stream()
                .collect(Collectors.toMap(
                        PlaceBookmarkContextRow::placeId,
                        PlaceBookmarkContextRow::context,
                        (a, b) -> a,              // 중복 방어 (이론상 없어야 함)
                        LinkedHashMap::new        // 순서 유지
                ));

        // Place 엔티티 한 번에 조회 (SQL 폭탄 )
        Map<Long, Place> placeMap =
                placeRepository.findAllById(orderedPlaceIds)
                        .stream()
                        .collect(Collectors.toMap(
                                Place::getId,
                                Function.identity()
                        ));

        // 최신순 placeId 기준으로 DTO 생성 (순서 보존)
        return orderedPlaceIds.stream()
                .map(placeId -> {
                    Place place = placeMap.get(placeId);
                    if (place == null) return null;

                    PlaceResDto dto = placeMapper.fromEntity(place, null);
                    dto.setBookmarkedContext(bookmarkContextMap.get(placeId));
                    return dto;
                })
                .filter(Objects::nonNull)
                .toList();

    }

    public boolean isBookmarked(Long userId, Long placeId) {
        return placeBookmarkRepository.existsByUserIdAndPlaceId(userId, placeId);
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
        // 최신 update 순으로 정렬된 row 조회
        List<PlaceLikedContextRow> rows =
                placeReactionRepository.findLikedContextsByUser(
                        userId,
                        ActionType.LIKE
                );

        if (rows.isEmpty()) {
            return List.of();
        }

        // placeId 기준으로 최신순 유지 + context 집합 생성
        LinkedHashMap<Long, Set<MbtiContext>> likedContextMap = new LinkedHashMap<>();

        for (PlaceLikedContextRow row : rows) {
            likedContextMap
                    .computeIfAbsent(row.placeId(), k -> new LinkedHashSet<>())
                    .add(row.context());
        }

        // 최신순 placeId 리스트 (순서가 핵심)
        List<Long> orderedPlaceIds = new ArrayList<>(likedContextMap.keySet());

        // Place 엔티티 한 번에 조회
        Map<Long, Place> placeMap =
                placeRepository.findAllById(orderedPlaceIds)
                        .stream()
                        .collect(Collectors.toMap(
                                Place::getId,
                                Function.identity()
                        ));

        // 최신순 기준으로 DTO 생성
        return orderedPlaceIds.stream()
                .map(placeId -> {
                    Place place = placeMap.get(placeId);
                    if (place == null) return null;

                    PlaceResDto dto = placeMapper.fromEntity(place, null);
                    dto.setLikedContexts(
                            likedContextMap.getOrDefault(placeId, Set.of())
                    );
                    return dto;
                })
                .filter(Objects::nonNull)
                .toList();

    }

    public List<PlaceViewCountDto> getMostViewedPlacesByMbti(
            String mbti,
            int limit
    ) {
        return userActionLogRepository.findMostViewedPlacesByEffectiveMbti(
                mbti,
                ActionType.VIEW,
                PageRequest.of(0, limit)
        );
    }

}

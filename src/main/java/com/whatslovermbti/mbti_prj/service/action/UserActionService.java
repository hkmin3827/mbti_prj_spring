package com.whatslovermbti.mbti_prj.service.action;

import com.whatslovermbti.mbti_prj.constant.*;
import com.whatslovermbti.mbti_prj.dto.place.PlaceSnapshot;
import com.whatslovermbti.mbti_prj.entity.*;
import com.whatslovermbti.mbti_prj.exception.CustomException;
import com.whatslovermbti.mbti_prj.repository.*;
import com.whatslovermbti.mbti_prj.service.place.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserActionService {
    private final PlaceKeywordRepository placeKeywordRepository;
    private final UserKeywordPreferenceRepository userKeywordPreferenceRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final PlaceBookmarkRepository placeBookmarkRepository;
    private final PlaceViewHistoryRepository placeViewHistoryRepository;
    private final PlaceResolver placeResolver;
    private final UserActionLogRepository userActionLogRepository;

    public Place onPlaceClicked(User user, PlaceSnapshot snapshot, MbtiContext context) {
        Place place = placeResolver.resolveAndEnsureKeywords(snapshot);
        recordView(user.getId(), place.getId(), context);


        return place;
    }

    public void bookmarkPlace(Long userId, Long placeId, MbtiContext context) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));


        if (placeBookmarkRepository.existsByUserIdAndPlaceIdAndTargetMbti(userId, placeId, context)) {
            return;
        }

        placeBookmarkRepository.save(
                new PlaceBookmark(user, place, context)
        );

        applyUserAction(user, place, ActionType.SAVE, context);
    }

    public void removeBookmark(Long userId, Long placeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));

        PlaceBookmark bookmark =
                placeBookmarkRepository.findByUserIdAndPlaceId(userId, placeId)
                        .orElseThrow(() -> new CustomException(ErrorCode.BOOKMARK_NOT_FOUND));

        placeBookmarkRepository.delete(bookmark);

        applyUserAction(user, place, ActionType.UNSAVE, bookmark.getTargetMbti());
    }


    public void recordView(Long userId, Long placeId, MbtiContext context) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        boolean alreadyViewedToday =
                placeViewHistoryRepository
                        .existsByUserAndPlaceAndTargetMbtiAndViewedAtBetween(
                                user, place, context, startOfDay, endOfDay
                        );
        PlaceViewHistory history =
                placeViewHistoryRepository
                        .findByUserAndPlaceAndTargetMbti(user, place, context)
                        .orElseGet(() -> new PlaceViewHistory(user, place, context));

        history.updateViewedAt();

        placeViewHistoryRepository.save(history);

        // 최근 20개 유지
        List<Long> ids =
                placeViewHistoryRepository
                        .findIdsByUserAndTargetMbtiOrderByViewedAtDesc(userId, context);
        if (ids.size() > 20) {
            placeViewHistoryRepository.deleteByIds(ids.subList(20, ids.size()));
        }

        if (!alreadyViewedToday) {
            applyUserAction(user, place, ActionType.VIEW, context);
        }
    }

    // viewHistory는 삭제해도 가중치 되돌리지 X, 내역을 지우고 싶은 목적이 장소가 마음에 안 들어서가 아니기 때문
    public void removeViewHistory(Long userId, Long placeId) {
        placeViewHistoryRepository.deleteByUserIdAndPlaceId(userId, placeId);
    }

    public void applyUserAction(User user, Place place, ActionType actionType, MbtiContext context) {

        // 하루 1회 제한
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        boolean alreadyLogged =
                userActionLogRepository
                        .existsByUserAndPlaceAndActionTypeAndTargetMbtiAndCreatedAtBetween(
                                user,
                                place,
                                actionType,
                                context,
                                startOfDay,
                                endOfDay
                        );

        if (alreadyLogged) {
            return; // 하루에 존재하면 로그 + 가중치 반영 전부 중단
        }

        userActionLogRepository.save(
                UserActionLog.of(user, place, actionType, context)
        );

        List<PlaceKeyword> placeKeywords =
                placeKeywordRepository.findByPlace(place);

        if (placeKeywords.isEmpty()) {
            log.info("THERE's NO placeKeywords");
            return;
        }
        double baseDelta = ActionWeightPolicy.getWeight(actionType);

        List<UserKeywordPreference> prefs =
                userKeywordPreferenceRepository.findByUserAndTargetMbti(user, context);
        Map<Long, UserKeywordPreference> prefMap =
                prefs.stream()
                        .collect(Collectors.toMap(
                                p -> p.getKeyword().getId(),
                                p -> p
                        ));

        for (PlaceKeyword pk : placeKeywords) {
            Keyword keyword = pk.getKeyword();

            UserKeywordPreference pref =
                    prefMap.computeIfAbsent(keyword.getId(), k -> {
                        UserKeywordPreference newPref = new UserKeywordPreference();
                        newPref.setUser(user);
                        newPref.setKeyword(keyword);
                        newPref.setTargetMbti(context);
                        newPref.setScore(0);
                        return newPref;
                    });

            double finalDelta = baseDelta * pk.getWeight();

            // 안전하게 점수 누적
            pref.setScore(
                    Math.max(-100, Math.min(100, pref.getScore() + finalDelta))
            );

            userKeywordPreferenceRepository.save(pref);
        }
    }
}


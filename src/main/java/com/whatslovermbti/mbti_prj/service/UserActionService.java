package com.whatslovermbti.mbti_prj.service;

import com.whatslovermbti.mbti_prj.constant.ActionType;
import com.whatslovermbti.mbti_prj.constant.ActionWeightPolicy;
import com.whatslovermbti.mbti_prj.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.entity.*;
import com.whatslovermbti.mbti_prj.exception.CustomException;
import com.whatslovermbti.mbti_prj.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    public void bookmarkPlace(Long userId, Long placeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));

        if (placeBookmarkRepository.existsByUserIdAndPlaceId(userId, placeId)) {
            return; // 이미 저장됨 → 무시 or 예외
        }

        PlaceBookmark bookmark = new PlaceBookmark(user, place);

        placeBookmarkRepository.save(bookmark);

        applyUserAction(userId, placeId, ActionType.SAVE);
    }

    public void removeBookmark(Long userId, Long placeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));

        PlaceBookmark bookmark =
                placeBookmarkRepository.findByUserAndPlace(user, place)
                        .orElseThrow(() -> new CustomException(ErrorCode.BOOKMARK_NOT_FOUND));

        // 북마크 삭제
        placeBookmarkRepository.delete(bookmark);

        // 가중치 되돌리기
        applyUserAction(userId, placeId, ActionType.UNSAVE);
    }

    public void recordView(Long userId, Long placeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));


        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();
        boolean alreadyViewedToday =
                placeViewHistoryRepository
                        .existsByUserAndPlaceAndViewedAtBetween(
                                user, place, startOfDay, endOfDay
                        );



        PlaceViewHistory history = new PlaceViewHistory(
                user, place
        );

        placeViewHistoryRepository.save(history);
        // 최근 20개 유지
        placeViewHistoryRepository.deleteOldViews(userId, 20);

        if (!alreadyViewedToday) {
            applyUserAction(userId, placeId, ActionType.VIEW);
        }
    }

    public void applyUserAction(Long userId, Long placeId, ActionType actionType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));

        int delta = ActionWeightPolicy.getWeight(actionType);

        List<PlaceKeyword> placeKeywords =
                placeKeywordRepository.findByPlace(place);

        // 키워드가 없는 장소면 로직 중단 (정책적으로 맞음)
        if (placeKeywords.isEmpty()) {
            return;
        }

        for (PlaceKeyword pk : placeKeywords) {
            Keyword keyword = pk.getKeyword();

            UserKeywordPreference pref =
                    userKeywordPreferenceRepository
                            .findByUserAndKeyword(user, keyword)
                            .orElseGet(() -> {
                                UserKeywordPreference newPref = new UserKeywordPreference();
                                newPref.setUser(user);
                                newPref.setKeyword(keyword);
                                newPref.setScore(0);
                                return newPref;
                            });


            // 점수 누적
            pref.setScore(pref.getScore() + delta);

            // 안전장치 (선택)
            pref.setScore(
                    Math.max(-100, Math.min(100, pref.getScore()))
            );

            userKeywordPreferenceRepository.save(pref);
        }
    }
}


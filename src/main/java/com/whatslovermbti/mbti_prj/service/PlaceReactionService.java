package com.whatslovermbti.mbti_prj.service;

import com.whatslovermbti.mbti_prj.constant.ActionType;
import com.whatslovermbti.mbti_prj.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.PlaceReaction;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.exception.CustomException;
import com.whatslovermbti.mbti_prj.repository.PlaceReactionRepository;
import com.whatslovermbti.mbti_prj.repository.PlaceRepository;
import com.whatslovermbti.mbti_prj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceReactionService {
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final PlaceReactionRepository placeReactionRepository;
    private final UserActionService userActionService;
    private static final Duration CLICK_COOLDOWN = Duration.ofSeconds(2);

    public void react(Long userId, Long placeId, ActionType type) {
        if (type != ActionType.LIKE && type != ActionType.DISLIKE) {
            throw new IllegalArgumentException("Invalid reaction type: " + type);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));

        placeReactionRepository
                .findByUserAndPlace(user, place)
                .ifPresentOrElse(existing -> {
                    if (existing.getType() == type &&
                            existing.getUpdatedAt() != null &&
                            existing.getUpdatedAt().isAfter(LocalDateTime.now().minusSeconds(2))) {
                        return;   // 2초내 같은 타입 연속 클릭 => 무시
                    }


                    ActionType oldType = existing.getType();

                    if (oldType == type) {
                        // 같은 반응 → 취소
                        placeReactionRepository.delete(existing);

                        userActionService.applyUserAction(userId, placeId,
                                invert(type)); // -delta
                    } else {
                        // 반응 전환
                        existing.setType(type);

                        userActionService.applyUserAction(userId, placeId,
                                invert(oldType)); // 기존 반응 제거
                        userActionService.applyUserAction(userId, placeId,
                                type);            // 새 반응 적용
                    }
                }, () -> {
                    // 최초 반응
                    placeReactionRepository.save(
                            new PlaceReaction(user, place, type)
                    );

                    userActionService.applyUserAction(userId, placeId, type);
                });
    }



    private ActionType invert(ActionType type) {
        if (type == ActionType.LIKE) return ActionType.DISLIKE;
        if (type == ActionType.DISLIKE) return ActionType.LIKE;
        throw new IllegalArgumentException("Reaction Invert not supported: " + type);
    }
}
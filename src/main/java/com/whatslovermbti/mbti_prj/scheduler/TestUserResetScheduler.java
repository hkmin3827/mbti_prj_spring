package com.whatslovermbti.mbti_prj.scheduler;

import com.whatslovermbti.mbti_prj.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestUserResetScheduler {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final PlaceBookmarkRepository placeBookmarkRepository;
    private final PlaceReactionRepository placeReactionRepository;
    private final PlaceViewHistoryRepository placeViewHistoryRepository;
    private final UserActionLogRepository userActionLogRepository;
    private final UserKeywordPreferenceRepository userKeywordPreferenceRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void resetTestAccountData() {
        String testAccountEmail = "test@mbti.com";

        userRepository.findByEmail(testAccountEmail).ifPresent(user -> {
            Long userId = user.getId();

            reviewRepository.deleteByUserId(userId);
            placeBookmarkRepository.deleteByUserId(userId);
            placeReactionRepository.deleteByUserId(userId);
            placeViewHistoryRepository.deleteByUserId(userId);
            userActionLogRepository.deleteByUserId(userId);
            userKeywordPreferenceRepository.deleteByUserId(userId);

            log.info("테스트 계정({}) 데이터 초기화 완료", testAccountEmail);
        });
    }
}

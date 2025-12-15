package com.whatslovermbti.mbti_prj.service;


import com.whatslovermbti.mbti_prj.constant.MbtiAxis;
import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.entity.MbtiKeywordWeight;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.entity.UserKeywordPreference;
import com.whatslovermbti.mbti_prj.repository.KeywordRepository;
import com.whatslovermbti.mbti_prj.repository.MbtiKeywordWeightRepository;
import com.whatslovermbti.mbti_prj.repository.UserKeywordPreferenceRepository;
import com.whatslovermbti.mbti_prj.service.recommendation.KeywordWeight;
import com.whatslovermbti.mbti_prj.service.weight.KeywordBehaviorWeightService;
import com.whatslovermbti.mbti_prj.service.weight.MbtiKeywordWeightService;
import com.whatslovermbti.mbti_prj.util.MbtiAxisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordWeightAggregator {

    private final KeywordRepository keywordRepository;
    private final UserKeywordPreferenceRepository userKeywordPreferenceRepository;
    private final MbtiKeywordWeightRepository mbtiKeywordWeightRepository;
    private final MbtiKeywordWeightService mbtiKeywordWeightService;
    private final KeywordBehaviorWeightService behaviorWeightService;

    /**
     * 유저 기준 최종 키워드 가중치 집계
     */
    public List<KeywordWeight> aggregate(User user) {

        String mbti = user.getMbti();
        Set<MbtiAxis> axes =
                MbtiAxisUtil.parseAxes(mbti).keySet().stream()
                        .map(MbtiAxis::valueOf)
                        .collect(Collectors.toSet());
        // ✅ 1번 쿼리
        List<Keyword> allKeywords = keywordRepository.findAll();

        // ✅ 1번 쿼리 (MBTI 가중치 전체)
        List<MbtiKeywordWeight> mbtiWeights =
                mbtiKeywordWeightRepository.findAllByAxes(axes);

        // ✅ 1번 쿼리 (유저 선호 전체)
        List<UserKeywordPreference> userPrefs =
                userKeywordPreferenceRepository.findAllByUser(user);

        // --- Map 변환 ---
        Map<Long, Integer> mbtiWeightMap =
                mbtiWeights.stream()
                        .collect(Collectors.groupingBy(
                                w -> w.getKeyword().getId(),
                                Collectors.summingInt(MbtiKeywordWeight::getWeight)
                        ));

        Map<Long, Integer> userPrefMap =
                userPrefs.stream()
                        .collect(Collectors.toMap(
                                p -> p.getKeyword().getId(),
                                UserKeywordPreference::getScore
                        ));

        List<KeywordWeight> result = new ArrayList<>();

        for (Keyword keyword : allKeywords) {

            // 🔁 기존 로직 그대로 (데이터 소스만 Map으로 변경)
            int mbtiWeight =
                    mbtiWeightMap.getOrDefault(keyword.getId(), 0);

            int userPref =
                    userPrefMap.getOrDefault(keyword.getId(), 0);

            double behaviorWeight =
                    behaviorWeightService.calculateBehaviorWeight(0, 0, 0);

            boolean isOpposite =
                    mbtiWeight < 0; // 기존 isOpposite 의미 유지

            double finalWeight =
                    mbtiKeywordWeightService.applyDilution(
                            mbtiWeight + userPref,
                            behaviorWeight,
                            isOpposite
                    );

            if (finalWeight > 0) {
                result.add(new KeywordWeight(
                        keyword.getName(),
                        finalWeight
                ));
            }
        }

        return result;
    }

    @Transactional(readOnly = true)
    public List<String> getTopKeywordNames(User user, int limit) {

        return aggregate(user).stream()
                .sorted((a, b) -> Double.compare(b.weight(), a.weight()))
                .limit(limit)
                .map(KeywordWeight::keyword)
                .toList();
    }
}
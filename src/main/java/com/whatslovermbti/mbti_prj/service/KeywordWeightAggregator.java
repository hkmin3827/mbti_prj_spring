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
import com.whatslovermbti.mbti_prj.service.weight.MbtiKeywordWeightPolicy;
import com.whatslovermbti.mbti_prj.util.MbtiAxisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static software.amazon.awssdk.profiles.ProfileFileSupplier.aggregate;

@Slf4j

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordWeightAggregator {

    private final KeywordRepository keywordRepository;
    private final MbtiKeywordWeightRepository mbtiKeywordWeightRepository;
    private final UserKeywordPreferenceRepository userKeywordPreferenceRepository;

    /**
     * MBTI 기준 키워드 기본 가중치 Map
     */
    public Map<Long, Integer> getMbtiWeightMap(String mbti) {

        Set<MbtiAxis> axes =
                MbtiAxisUtil.parseAxes(mbti)
                        .keySet()
                        .stream()
                        .map(MbtiAxis::valueOf)
                        .collect(Collectors.toSet());

        // ^^^^엠비티아이별로 바뀌는지 확인 로그
        log.info("[MBTI_AXES] mbti={}, axes={}", mbti, axes);

        List<MbtiKeywordWeight> weights =
                mbtiKeywordWeightRepository.findAllByAxes(axes);


        // ^^^^키워드 몇 개만 샘플 로그
        Map<Long, Integer> map =
                weights.stream()
                        .collect(Collectors.groupingBy(
                                w -> w.getKeyword().getId(),
                                Collectors.summingInt(MbtiKeywordWeight::getWeight)
                        ));
        map.entrySet().stream()
                .limit(5)
                .forEach(e ->
                        log.info("[MBTI_WEIGHT] keywordId={}, weight={}", e.getKey(), e.getValue())
                );
        // ^^^^여기까지 로그


        return weights.stream()
                .collect(Collectors.groupingBy(
                        w -> w.getKeyword().getId(),
                        Collectors.summingInt(MbtiKeywordWeight::getWeight)
                ));
    }

    /**
     * 유저 행동 기반 키워드 선호 Map
     */
    public Map<Long, Integer> getUserPreferenceMap(User user) {

        return userKeywordPreferenceRepository.findAllByUser(user).stream()
                .collect(Collectors.toMap(
                        p -> p.getKeyword().getId(),
                        UserKeywordPreference::getScore
                ));
    }


    /**
     * 전체 키워드 조회
     */
    public List<Keyword> getAllKeywords() {
        return keywordRepository.findAll();
    }

    /**
     * 장소 후보군 확장을 위한 상위 키워드 목록
     * - MBTI 기본 가중치 + 유저 선호 점수 기준
     * - 정책/희석 적용 ❌
     */
    public List<String> getTopKeywordNames(User user, int limit) {

        Map<Long, Integer> mbtiWeightMap =
                getMbtiWeightMap(user.getMbti());

        Map<Long, Integer> userPrefMap =
                getUserPreferenceMap(user);

        return getAllKeywords().stream()
                .map(keyword -> {
                    int mbtiWeight =
                            mbtiWeightMap.getOrDefault(keyword.getId(), 0);

                    int userPref =
                            userPrefMap.getOrDefault(keyword.getId(), 0);

                    int baseScore = mbtiWeight + userPref;

                    return Map.entry(keyword.getName(), baseScore);
                })
                // 점수 없는 키워드는 제외
                .filter(e -> e.getValue() > 0)
                // 점수 내림차순
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
    }
}

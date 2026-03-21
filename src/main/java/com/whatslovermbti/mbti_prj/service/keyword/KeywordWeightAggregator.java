package com.whatslovermbti.mbti_prj.service.keyword;

import com.whatslovermbti.mbti_prj.constant.MbtiAxis;
import com.whatslovermbti.mbti_prj.entity.MbtiKeywordWeight;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.entity.UserKeywordPreference;
import com.whatslovermbti.mbti_prj.repository.MbtiKeywordWeightRepository;
import com.whatslovermbti.mbti_prj.repository.UserKeywordPreferenceRepository;
import com.whatslovermbti.mbti_prj.util.MbtiAxisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordWeightAggregator {

    private final MbtiKeywordWeightRepository mbtiKeywordWeightRepository;
    private final UserKeywordPreferenceRepository userKeywordPreferenceRepository;

    public Map<String, Integer> getMbtiKeywordWeightMapByName(String mbti) {

        Set<MbtiAxis> axes =
                MbtiAxisUtil.parseAxes(mbti)
                        .keySet()
                        .stream()
                        .map(MbtiAxis::valueOf)
                        .collect(Collectors.toSet());

        log.info("[MBTI_AXES] mbti={}, axes={}", mbti, axes);

        List<MbtiKeywordWeight> weights =
                mbtiKeywordWeightRepository.findAllByAxes(axes);


        Map<String, Integer> map =
                weights.stream()
                        .collect(Collectors.groupingBy(
                                w -> w.getKeyword().getName(),
                                Collectors.summingInt(MbtiKeywordWeight::getWeight)
                        ));

        map.forEach((k, v) ->
                log.info("[MBTI_WEIGHT] keywordName={}, weight={}", k, v)
        );
        return map;
    }

    public Map<String, Double> getUserKeywordPreferenceMapByName(User user) {
        return userKeywordPreferenceRepository.findAllByUser(user).stream()
                .collect(Collectors.toMap(
                        p -> p.getKeyword().getName(),
                        UserKeywordPreference::getScore,
                        Double::sum
                ));
    }


    public Map<String, Double> getCombinedKeywordWeightMapByName(
            User user,
            String targetMbti
    ) {
        Map<String, Double> result = new HashMap<>();

        Map<String, Integer> mbtiWeights = getMbtiKeywordWeightMapByName(targetMbti);
        Map<String, Double> userPrefWeights = getUserKeywordPreferenceMapByName(user);

        mbtiWeights.forEach((k, v) -> result.merge(k,  v.doubleValue(), Double::sum));
        userPrefWeights.forEach((k, v) -> result.merge(k, v, Double::sum));

        result.entrySet().removeIf(e -> Math.abs(e.getValue()) < 0.0001);

        return result;
    }
}

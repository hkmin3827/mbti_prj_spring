package com.whatslovermbti.mbti_prj.service.place;

import com.whatslovermbti.mbti_prj.constant.PlaceSubCategory;
import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.PlaceKeyword;
import com.whatslovermbti.mbti_prj.repository.KeywordRepository;
import com.whatslovermbti.mbti_prj.repository.PlaceKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceKeywordMapper {

    private final KeywordRepository keywordRepository;
    private final PlaceKeywordRepository placeKeywordRepository;

    /**
     * Place 최초 저장 / 행동 발생 시점에만 호출
     */
    @Transactional
    public void mapKeywords(
            Place place,
            Set<PlaceSubCategory> subCategories,
            List<String> inferredKeywords
    ) {

        Map<String, Integer> keywordWeights = new HashMap<>();

        /* ================= 공통 ================= */
        add(keywordWeights, "데이트", 1);

        /* ================= SubCategory 기반 보정 ================= */

        if (hasAny(subCategories,
                PlaceSubCategory.RESTAURANT,
                PlaceSubCategory.FINE_DINING)) {

            add(keywordWeights, "계획적인", 2);
            add(keywordWeights, "분위기좋은", 2);
        }

        if (hasAny(subCategories,
                PlaceSubCategory.BAR,
                PlaceSubCategory.IZAKAYA,
                PlaceSubCategory.PUB,
                PlaceSubCategory.POCHA)) {

            add(keywordWeights, "즉흥적인", 2);
            add(keywordWeights, "친밀한", 2);
        }

        if (subCategories.contains(PlaceSubCategory.QUICK_MEAL)) {
            add(keywordWeights, "가성비", 2);
        }

        if (subCategories.contains(PlaceSubCategory.STUDY_CAFE)) {
            add(keywordWeights, "논리적인", 2);
            add(keywordWeights, "조용한", 2);
        }

        if (hasAny(subCategories,
                PlaceSubCategory.PARK,
                PlaceSubCategory.WALK,
                PlaceSubCategory.VIEW)) {

            add(keywordWeights, "자유로운", 2);
        }

        if (hasAny(subCategories,
                PlaceSubCategory.ACTIVITY,
                PlaceSubCategory.GAME,
                PlaceSubCategory.SPORTS)) {

            add(keywordWeights, "활동적인", 2);
        }

        /* ================= Inferer 결과 병합 ================= */

        if (inferredKeywords != null) {
            for (String k : inferredKeywords) {
                add(keywordWeights, k, 1);
            }
        }

        if (keywordWeights.isEmpty()) return;

        /* ================= DB 처리 (벌크) ================= */

        List<String> names = new ArrayList<>(keywordWeights.keySet());

        List<Keyword> keywords = keywordRepository.findByNameIn(names);

        Map<String, Keyword> keywordMap =
                keywords.stream()
                        .collect(Collectors.toMap(Keyword::getName, k -> k));

        List<PlaceKeyword> placeKeywords = new ArrayList<>();

        for (Map.Entry<String, Integer> e : keywordWeights.entrySet()) {
            Keyword keyword = keywordMap.get(e.getKey());
            if (keyword == null) continue;

            placeKeywords.add(
                    new PlaceKeyword(place, keyword, e.getValue())
            );
        }

        placeKeywordRepository.saveAll(placeKeywords);
    }

    /* ================= util ================= */

    private void add(Map<String, Integer> map, String keyword, int weight) {
        map.merge(keyword, weight, Math::max);
    }

    private boolean hasAny(Set<PlaceSubCategory> set, PlaceSubCategory... targets) {
        for (PlaceSubCategory t : targets) {
            if (set.contains(t)) return true;
        }
        return false;
    }
}

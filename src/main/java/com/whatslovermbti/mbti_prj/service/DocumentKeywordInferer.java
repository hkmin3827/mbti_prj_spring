package com.whatslovermbti.mbti_prj.service;
// Document -> 키워드 추론기

import com.whatslovermbti.mbti_prj.constant.PlaceSubCategory;
import com.whatslovermbti.mbti_prj.dto.place.PlaceSnapshot;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class DocumentKeywordInferer {
    public List<String> infer(
            PlaceSnapshot snapshot,
            Set<PlaceSubCategory> subCategories
    ) {

        List<String> keywords = new ArrayList<>();

        if (subCategories == null || subCategories.isEmpty()) {
            return keywords;
        }

        /* ================= 공통 ================= */
        keywords.add("데이트");

        /* ================= 음식점 ================= */

        if (hasAny(subCategories,
                PlaceSubCategory.RESTAURANT,
                PlaceSubCategory.FINE_DINING)) {

            keywords.add("계획적인");
            keywords.add("분위기좋은");
        }

        if (hasAny(subCategories,
                PlaceSubCategory.BAR,
                PlaceSubCategory.IZAKAYA,
                PlaceSubCategory.PUB,
                PlaceSubCategory.POCHA)) {

            keywords.add("즉흥적인");
            keywords.add("친밀한");
        }

        if (subCategories.contains(PlaceSubCategory.QUICK_MEAL)) {
            keywords.add("가성비");
        }

        /* ================= 카페 ================= */

        if (hasAny(subCategories,
                PlaceSubCategory.BAKERY,
                PlaceSubCategory.DESSERT,
                PlaceSubCategory.EMOTIONAL)) {

            keywords.add("감성적인");
            keywords.add("조용한");
        }

        if (subCategories.contains(PlaceSubCategory.STUDY_CAFE)) {
            keywords.add("논리적인");
            keywords.add("조용한");
        }

        /* ================= COURSE ================= */

        if (hasAny(subCategories,
                PlaceSubCategory.PARK,
                PlaceSubCategory.WALK,
                PlaceSubCategory.VIEW)) {

            keywords.add("자유로운");
            keywords.add("편안한");
        }

        if (hasAny(subCategories,
                PlaceSubCategory.MUSEUM,
                PlaceSubCategory.EXHIBITION,
                PlaceSubCategory.CULTURE)) {

            keywords.add("차분한");
            keywords.add("사색적인");
        }

        if (hasAny(subCategories,
                PlaceSubCategory.ACTIVITY,
                PlaceSubCategory.GAME,
                PlaceSubCategory.SPORTS)) {

            keywords.add("활동적인");
            keywords.add("에너지있는");
        }

        return keywords;
    }

    /* ================= util ================= */

    private boolean hasAny(Set<PlaceSubCategory> set, PlaceSubCategory... targets) {
        for (PlaceSubCategory t : targets) {
            if (set.contains(t)) {
                return true;
            }
        }
        return false;
    }
}
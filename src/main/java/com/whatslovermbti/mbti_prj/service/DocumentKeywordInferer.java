package com.whatslovermbti.mbti_prj.service;
// Document -> 키워드 추출기

import com.whatslovermbti.mbti_prj.constant.Category;
import com.whatslovermbti.mbti_prj.constant.PlaceSubCategory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class DocumentKeywordInferer {
    public List<String> infer(
            Set<PlaceSubCategory> subCategories,
            Category category
    ) {
        List<String> keywords = new ArrayList<>();

        if (subCategories == null || subCategories.isEmpty()) {
            return keywords;
        }

        /* ================= CATEGORY 기본 ================= */
        switch (category) {
            case CAFE -> {
                add(keywords, "감성적인");
                add(keywords, "아늑한");
                add(keywords, "자유로운");
                add(keywords, "조용한");
            }
            case FOOD -> {
                add(keywords, "분위기좋은");
                add(keywords, "계획적인");
                add(keywords, "실용적인");
            }
            case COURSE -> {
                add(keywords, "자유로운");
                add(keywords, "로맨틱한");
                add(keywords, "감성적인");
            }
        }

        /* ================= SubCategory 기반 ================= */

        if (hasAny(subCategories,
                PlaceSubCategory.RESTAURANT,
                PlaceSubCategory.FINE_DINING)) {

            add(keywords, "분위기좋은");
            add(keywords, "계획적인");
            add(keywords, "예약가능");
            add(keywords, "깔끔한");
        }

        if (hasAny(subCategories,
                PlaceSubCategory.BAR,
                PlaceSubCategory.IZAKAYA,
                PlaceSubCategory.PUB,
                PlaceSubCategory.POCHA)) {

            add(keywords, "즉흥적인");
            add(keywords, "따뜻한");
            add(keywords, "분위기좋은");
        }

        if (subCategories.contains(PlaceSubCategory.QUICK_MEAL)) {
            add(keywords, "가성비좋은");
            add(keywords, "실용적인");
        }

        if (subCategories.contains(PlaceSubCategory.STUDY_CAFE)) {
            add(keywords, "조용한");
            add(keywords, "논리적인");
            add(keywords, "깔끔한");
        }

        if (hasAny(subCategories,
                PlaceSubCategory.PARK,
                PlaceSubCategory.WALK,
                PlaceSubCategory.VIEW)) {

            add(keywords, "자유로운");
            add(keywords, "감성적인");
            add(keywords, "조용한");
        }

        if (hasAny(subCategories,
                PlaceSubCategory.ACTIVITY,
                PlaceSubCategory.GAME,
                PlaceSubCategory.SPORTS)) {

            add(keywords, "활동적인");
            add(keywords, "즉흥적인");
        }

        if (hasAny(subCategories,
                PlaceSubCategory.MUSEUM,
                PlaceSubCategory.EXHIBITION,
                PlaceSubCategory.CULTURE,
                PlaceSubCategory.PERFORMANCE)) {

            add(keywords, "감각적인");
            add(keywords, "감성적인");
            add(keywords, "조용한");
        }

        if (subCategories.contains(PlaceSubCategory.ROOFTOP)) {
            add(keywords, "화려한");
            add(keywords, "분위기좋은");
            add(keywords, "로맨틱한");
        }

        return keywords;
    }

    /* ================= util ================= */
    private void add(List<String> list, String keyword) {
        if (!list.contains(keyword)) {
            list.add(keyword);
        }
    }

    private boolean hasAny(Set<PlaceSubCategory> set, PlaceSubCategory... targets) {
        for (PlaceSubCategory t : targets) {
            if (set.contains(t)) {
                return true;
            }
        }
        return false;
    }
}
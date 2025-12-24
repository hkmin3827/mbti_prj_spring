package com.whatslovermbti.mbti_prj.service.place;

import com.whatslovermbti.mbti_prj.constant.PlaceSubCategory;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;

@Component
public class SubCategoryResolver {

    public Set<PlaceSubCategory> resolveFromCategoryName(String categoryName) {

        Set<PlaceSubCategory> result = EnumSet.noneOf(PlaceSubCategory.class);

        if (categoryName == null) {
            return result;
        }

        /* ===================== FD6 : 음식점 ===================== */

        if (categoryName.contains("레스토랑")) {
            result.add(PlaceSubCategory.RESTAURANT);
        }

        if (categoryName.contains("파인다이닝") || categoryName.contains("오마카세")) {
            result.add(PlaceSubCategory.FINE_DINING);
        }

        if (categoryName.contains("술집") || categoryName.contains("주점")) {
            result.add(PlaceSubCategory.BAR);
        }

        if (categoryName.contains("이자카야")) {
            result.add(PlaceSubCategory.IZAKAYA);
        }

        if (categoryName.contains("포장마차")) {
            result.add(PlaceSubCategory.POCHA);
        }

        if (categoryName.contains("호프") || categoryName.contains("펍")) {
            result.add(PlaceSubCategory.PUB);
        }

        if (categoryName.contains("뷔페")) {
            result.add(PlaceSubCategory.BUFFET);
        }

        if (categoryName.contains("패스트푸드")) {
            result.add(PlaceSubCategory.FAST_CASUAL);
        }

        if (categoryName.contains("분식")
                || categoryName.contains("백반")
                || categoryName.contains("국밥")
                || categoryName.contains("김밥")) {
            result.add(PlaceSubCategory.QUICK_MEAL);
        }

        /* ===================== CE7 : 카페 ===================== */

        if (categoryName.contains("베이커리")) {
            result.add(PlaceSubCategory.BAKERY);
        }

        if (categoryName.contains("디저트")) {
            result.add(PlaceSubCategory.DESSERT);
        }

        if (categoryName.contains("스터디카페") || categoryName.contains("북카페")) {
            result.add(PlaceSubCategory.STUDY_CAFE);
        }

        if (categoryName.contains("카페")
                && !categoryName.contains("스터디")
                && !categoryName.contains("북카페")) {
            // 일반 카페 성격 보정용
            result.add(PlaceSubCategory.EMOTIONAL);
        }

    /* ⚠️ ROOFTOP / TERRACE / WORK_CAFE
       → category_name에는 거의 안 나옴
       → place_name 없이는 추론 금지
    */

        /* ===================== COURSE : AT4 / CT1 / LEI ===================== */

        if (categoryName.contains("공원")) {
            result.add(PlaceSubCategory.PARK);
        }

        if (categoryName.contains("산책")
                || categoryName.contains("둘레길")) {
            result.add(PlaceSubCategory.WALK);
        }

        if (categoryName.contains("전망")
                || categoryName.contains("전망대")) {
            result.add(PlaceSubCategory.VIEW);
        }

        if (categoryName.contains("미술관")
                || categoryName.contains("박물관")) {
            result.add(PlaceSubCategory.MUSEUM);
            result.add(PlaceSubCategory.CULTURE);
        }

        if (categoryName.contains("전시관")
                || categoryName.contains("갤러리")) {
            result.add(PlaceSubCategory.EXHIBITION);
            result.add(PlaceSubCategory.CULTURE);
        }

        if (categoryName.contains("공연장")
                || categoryName.contains("콘서트")) {
            result.add(PlaceSubCategory.PERFORMANCE);
            result.add(PlaceSubCategory.CULTURE);
        }

        if (categoryName.contains("영화관")
                || categoryName.contains("극장")) {
            result.add(PlaceSubCategory.CINEMA);
            result.add(PlaceSubCategory.CULTURE);
        }

        if (categoryName.contains("체험")
                || categoryName.contains("레저")
                || categoryName.contains("놀이시설")) {
            result.add(PlaceSubCategory.ACTIVITY);
        }

        if (categoryName.contains("보드게임")
                || categoryName.contains("방탈출")
                || categoryName.contains("VR")) {
            result.add(PlaceSubCategory.GAME);
            result.add(PlaceSubCategory.ACTIVITY);
        }

        if (categoryName.contains("스포츠")
                || categoryName.contains("볼링장")
                || categoryName.contains("당구장")) {
            result.add(PlaceSubCategory.SPORTS);
            result.add(PlaceSubCategory.ACTIVITY);
        }

        return result;
    }


    private boolean contains(String target, String... words) {
        if (target == null) return false;
        for (String w : words) {
            if (target.contains(w)) return true;
        }
        return false;
    }
}

//package com.whatslovermbti.mbti_prj.service;
//
//import com.whatslovermbti.mbti_prj.constant.Category;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CategoryMapper {
//
//    /**
//     * Kakao category_name → 내부 Category
//     *
//     * 기준:
//     * - 카페 → CAFE
//     * - 음식점/레스토랑 → FOOD
//     * - 나머지 전부 → COURSE (데이트 장소)
//     */
//    public Category resolveCategory(String kakaoCategoryName) {
//
//        if (kakaoCategoryName == null) {
//            return Category.COURSE;
//        }
//
//        // ☕ 카페
//        if (kakaoCategoryName.contains("카페")) {
//            return Category.CAFE;
//        }
//
//        // 🍽️ 음식점
//        if (kakaoCategoryName.contains("음식점")
//                || kakaoCategoryName.contains("레스토랑") || kakaoCategoryName.contains("술집")) {
//            return Category.FOOD;
//        }
//
//        // 🎯 그 외 전부 데이트 코스
//        // (전시, 미술관, 관광, 공원, 액티비티 등)
//        return Category.COURSE;
//    }
//}

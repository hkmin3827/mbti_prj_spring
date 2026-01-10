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
     * Place 최초 생성 시 호출
     * - infer 결과로 "선택된 키워드"에 대해
     * - category / subCategory 기반 기본 가중치만 부여
     */
    @Transactional
    public void mapInitialKeywords(
            Place place,
            Set<PlaceSubCategory> subCategories,
            List<String> inferredKeywords
    ) {
        if (inferredKeywords == null || inferredKeywords.isEmpty()) {
            return;
        }


        Map<String, Integer> keywordWeights = new HashMap<>();

        /* ================= CATEGORY 기반 가중치 ================= */
        switch (place.getCategory()) {
            case CAFE -> {
                boost(keywordWeights, inferredKeywords,
                        Map.of(
                                "감성적인", 2,
                                "아늑한", 1,
                                "조용한", 1,
                                "자유로운", 1
                        )
                );
            }
            case FOOD -> {
                boost(keywordWeights, inferredKeywords,
                        Map.of(
                                "분위기좋은", 2,
                                "계획적인", 1,
                                "실용적인", 1
                        )
                );
            }
            case COURSE -> {
                boost(keywordWeights, inferredKeywords,
                        Map.of(
                                "자유로운", 2,
                                "로맨틱한", 1,
                                "감성적인", 1
                        )
                );
            }
        }

        /* ================= SubCategory 기반 가중치 ================= */

        if (hasAny(subCategories,
                PlaceSubCategory.RESTAURANT,
                PlaceSubCategory.FINE_DINING)) {

            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "분위기좋은", 2,
                            "계획적인", 2,
                            "예약가능", 1,
                            "깔끔한", 1
                    )
            );
        }

        if (hasAny(subCategories,
                PlaceSubCategory.BAR,
                PlaceSubCategory.IZAKAYA,
                PlaceSubCategory.PUB,
                PlaceSubCategory.POCHA)) {

            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "즉흥적인", 2,
                            "따뜻한", 2,
                            "분위기좋은", 1
                    )
            );
        }

        if (subCategories.contains(PlaceSubCategory.QUICK_MEAL)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "가성비좋은", 2,
                            "실용적인", 1
                    )
            );
        }

        if (subCategories.contains(PlaceSubCategory.STUDY_CAFE)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "조용한", 2,
                            "논리적인", 2,
                            "깔끔한", 1
                    )
            );
        }

        if (hasAny(subCategories,
                PlaceSubCategory.PARK,
                PlaceSubCategory.WALK,
                PlaceSubCategory.VIEW)) {

            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "자유로운", 2,
                            "감성적인", 1,
                            "조용한", 1
                    )
            );
        }

        if (hasAny(subCategories,
                PlaceSubCategory.ACTIVITY,
                PlaceSubCategory.GAME,
                PlaceSubCategory.SPORTS)) {

            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "활동적인", 3,
                            "즉흥적인", 1
                    )
            );
        }

        if (hasAny(subCategories,
                PlaceSubCategory.MUSEUM,
                PlaceSubCategory.EXHIBITION,
                PlaceSubCategory.CULTURE,
                PlaceSubCategory.PERFORMANCE)) {

            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "감각적인", 2,
                            "감성적인", 1,
                            "조용한", 1
                    )
            );
        }

        if (subCategories.contains(PlaceSubCategory.ROOFTOP)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "화려한", 2,
                            "분위기좋은", 1,
                            "로맨틱한", 1
                    )
            );

        }
        /* ---------- 카페 계열 ---------- */

        // 디저트카페/디저트: N/F 쪽 + 감성/로맨틱
        if (hasAny(subCategories, PlaceSubCategory.DESSERT, PlaceSubCategory.DESSERT_CAFE)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "감성적인", 2,
                            "분위기좋은", 2,
                            "로맨틱한", 1,
                            "감각적인", 1
                    )
            );
        }

        // 전통찻집: I/N/F 성향(정적/여백/대화) + 조용/따뜻/감성
        if (subCategories.contains(PlaceSubCategory.TRADITIONAL_TEA_HOUSE)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "조용한", 2,
                            "따뜻한", 2,
                            "감성적인", 1,
                            "분위기좋은", 1
                    )
            );
        }

        // 무인카페: I/T 성향(혼자/효율) + 조용/실용/깔끔
        if (subCategories.contains(PlaceSubCategory.UNMANNED_CAFE)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "조용한", 2,
                            "실용적인", 2,
                            "깔끔한", 1,
                            "논리적인", 1
                    )
            );
        }

        // 북카페: I/N/T/F 혼합이지만 기본은 정적/집중 + 조용/감성/깔끔
        if (subCategories.contains(PlaceSubCategory.BOOK_CAFE)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "조용한", 2,
                            "감성적인", 1,
                            "깔끔한", 1,
                            "논리적인", 1
                    )
            );
        }

        // 갤러리카페: N(미적) + 감각/분위기/조용
        if (subCategories.contains(PlaceSubCategory.GALLERY_CAFE)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "감각적인", 2,
                            "분위기좋은", 2,
                            "조용한", 1,
                            "감성적인", 1
                    )
            );
        }

        // 애견카페: F/E 성향(교감/소통) + 따뜻/활동/분위기
        if (subCategories.contains(PlaceSubCategory.PET_CAFE)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "따뜻한", 2,
                            "활동적인", 1,
                            "분위기좋은", 1,
                            "즉흥적인", 1
                    )
            );
        }

        // 키즈카페: E/P + 활동/즉흥/따뜻(가족/케어)
        if (subCategories.contains(PlaceSubCategory.KIDS_CAFE)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "활동적인", 3,
                            "즉흥적인", 1,
                            "따뜻한", 1
                    )
            );
        }

        // 라이브카페: E/N/F 성향(분위기/감정/공연) + 화려/분위기/로맨틱
        if (subCategories.contains(PlaceSubCategory.LIVE_CAFE)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "분위기좋은", 2,
                            "화려한", 2,
                            "로맨틱한", 1,
                            "감성적인", 1
                    )
            );
        }

        // 보드카페: E/P + 활동/즉흥 + (T 성향도 일부: 게임 규칙)
        if (subCategories.contains(PlaceSubCategory.BOARD_CAFE)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "활동적인", 2,
                            "즉흥적인", 2,
                            "논리적인", 1
                    )
            );
        }

        // 다방: S/F + 레트로/편안함 = 따뜻/감성/조용
        if (subCategories.contains(PlaceSubCategory.DABANG)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "따뜻한", 2,
                            "감성적인", 1,
                            "조용한", 1
                    )
            );
        }

        // 생과일전문점: S 성향(건강/현실) + 실용/깔끔/가성비
        if (subCategories.contains(PlaceSubCategory.FRESH_FRUIT_SHOP)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "실용적인", 2,
                            "깔끔한", 1,
                            "가성비좋은", 1
                    )
            );
        }

        // 카페거리: P/N 성향(탐색/산책/핫플) + 자유/감성/즉흥
        if (subCategories.contains(PlaceSubCategory.CAFE_STREET)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "자유로운", 2,
                            "즉흥적인", 1,
                            "감성적인", 1,
                            "분위기좋은", 1
                    )
            );
        }

        /* ---------- 맛집(음식) 디테일 ---------- */

        // 한식: S/F 기반(편안/정서) + 따뜻/가성비/실용
        if (subCategories.contains(PlaceSubCategory.KOREAN)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "따뜻한", 2,
                            "실용적인", 1,
                            "가성비좋은", 1
                    )
            );
        }

        // 일식/오마카세 쪽: J/T(정갈/예약/정리) + 깔끔/계획/분위기
        if (subCategories.contains(PlaceSubCategory.JAPANESE)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "깔끔한", 2,
                            "계획적인", 1,
                            "분위기좋은", 1
                    )
            );
        }

        // 양식: N/F(데이트 무드) + 분위기/로맨틱/감각
        if (subCategories.contains(PlaceSubCategory.WESTERN)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "분위기좋은", 2,
                            "로맨틱한", 1,
                            "감각적인", 1
                    )
            );
        }

        // 중식: E/S 성향(모임/활기) + 활동/가성비/즉흥
        if (subCategories.contains(PlaceSubCategory.CHINESE)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "활동적인", 1,
                            "가성비좋은", 1,
                            "즉흥적인", 1
                    )
            );
        }

        // 아시아음식: N/P(새로움/도전) + 감각/즉흥
        if (subCategories.contains(PlaceSubCategory.ASIAN_FOOD)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "감각적인", 2,
                            "즉흥적인", 1,
                            "자유로운", 1
                    )
            );
        }

        // 철판요리: E/P(퍼포먼스/현장감) + 화려/활동/즉흥
        if (subCategories.contains(PlaceSubCategory.TEPPANYAKI)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "화려한", 2,
                            "활동적인", 1,
                            "즉흥적인", 1
                    )
            );
        }

        // 샤브샤브: J/S(정리/무난) + 깔끔/계획/따뜻
        if (subCategories.contains(PlaceSubCategory.SHABU_SHABU)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "깔끔한", 2,
                            "계획적인", 1,
                            "따뜻한", 1
                    )
            );
        }

        // 치킨/호프: E/P(모임/가벼움) + 즉흥/활동/따뜻
        if (subCategories.contains(PlaceSubCategory.CHICKEN)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "즉흥적인", 2,
                            "활동적인", 1,
                            "따뜻한", 1
                    )
            );
        }

        // 샐러드/도시락: S/T(관리/효율) + 실용/깔끔/논리
        if (hasAny(subCategories, PlaceSubCategory.SALAD, PlaceSubCategory.LUNCHBOX)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "실용적인", 2,
                            "깔끔한", 2,
                            "논리적인", 1
                    )
            );
        }

        // 간식/푸드코트: P/S(가볍게/빠르게) + 가성비/즉흥/실용
        if (hasAny(subCategories, PlaceSubCategory.SNACK, PlaceSubCategory.FOOD_COURT)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "가성비좋은", 2,
                            "즉흥적인", 1,
                            "실용적인", 1
                    )
            );
        }

        // 오뎅바: I/F(잔잔한 술자리) + 따뜻/조용/분위기
        if (subCategories.contains(PlaceSubCategory.ODEN_BAR)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "따뜻한", 2,
                            "조용한", 1,
                            "분위기좋은", 1
                    )
            );
        }

        // 와인바/칵테일바: N/F + 로맨틱/분위기/감각 (+J 예약)
        if (hasAny(subCategories, PlaceSubCategory.WINE_BAR, PlaceSubCategory.COCKTAIL_BAR)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "분위기좋은", 2,
                            "로맨틱한", 2,
                            "감각적인", 1,
                            "계획적인", 1
                    )
            );
        }

        // 요리주점: E/N/F(분위기+대화) + 따뜻/분위기/즉흥
        if (subCategories.contains(PlaceSubCategory.GASTRO_PUB)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "분위기좋은", 2,
                            "따뜻한", 1,
                            "즉흥적인", 1
                    )
            );
        }

        // 실내포장마차: E/P + 즉흥/활동/따뜻
        if (subCategories.contains(PlaceSubCategory.INDOOR_POCHA)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "즉흥적인", 3,
                            "활동적인", 1,
                            "따뜻한", 1
                    )
            );
        }

        /* ---------- 코스/여행/문화/자연 디테일 ---------- */

        // 먹자골목: E/P + 즉흥/활동/가성비
        if (subCategories.contains(PlaceSubCategory.FOOD_ALLEY)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "즉흥적인", 2,
                            "활동적인", 1,
                            "가성비좋은", 1,
                            "자유로운", 1
                    )
            );
        }

        // 테마파크/유원지: E/P + 활동/화려/즉흥
        if (hasAny(subCategories, PlaceSubCategory.THEME_PARK, PlaceSubCategory.AMUSEMENT_PARK)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "활동적인", 3,
                            "화려한", 2,
                            "즉흥적인", 1
                    )
            );
        }

        // 도보여행/자전거여행: P + 자유/활동 (+조용은 상황 따라)
        if (hasAny(subCategories, PlaceSubCategory.WALKING_TOUR, PlaceSubCategory.BIKE_TRIP)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "자유로운", 2,
                            "활동적인", 2,
                            "즉흥적인", 1
                    )
            );
        }

        // 수목원/숲/계곡/저수지: I/N + 조용/감성/자유
        if (hasAny(subCategories,
                PlaceSubCategory.ARBORETUM,
                PlaceSubCategory.FOREST,
                PlaceSubCategory.VALLEY,
                PlaceSubCategory.RESERVOIR)) {

            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "조용한", 2,
                            "감성적인", 2,
                            "자유로운", 1
                    )
            );
        }

        // 산/전망대: I/N + 조용/자유/감각
        if (hasAny(subCategories, PlaceSubCategory.MOUNTAIN, PlaceSubCategory.OBSERVATORY)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "자유로운", 2,
                            "조용한", 1,
                            "감각적인", 1
                    )
            );
        }

        // 온천: I/S/F + 조용/따뜻/힐링(편안한)
        if (subCategories.contains(PlaceSubCategory.HOT_SPRING)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "조용한", 2,
                            "따뜻한", 2,
                            "실용적인", 1
                    )
            );
        }

        // 해변/섬: P/N + 자유/감성/로맨틱
        if (hasAny(subCategories, PlaceSubCategory.BEACH, PlaceSubCategory.ISLAND)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "자유로운", 2,
                            "감성적인", 2,
                            "로맨틱한", 1
                    )
            );
        }

        // 아쿠아리움: N/F(감성+체험) + 감각/로맨틱/조용
        if (subCategories.contains(PlaceSubCategory.AQUARIUM)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "감각적인", 2,
                            "로맨틱한", 1,
                            "조용한", 1,
                            "분위기좋은", 1
                    )
            );
        }

        // 문화유적/기념관: I/J/N + 조용/계획/감성
        if (hasAny(subCategories, PlaceSubCategory.CULTURAL_HERITAGE, PlaceSubCategory.MEMORIAL_HALL)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "조용한", 2,
                            "계획적인", 1,
                            "감성적인", 1
                    )
            );
        }

        // 도자기/도예촌: N/F + 감성/감각 (+조용)
        if (subCategories.contains(PlaceSubCategory.POTTERY_VILLAGE)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "감각적인", 2,
                            "감성적인", 2,
                            "조용한", 1
                    )
            );
        }

        // 과학관: T/N + 논리/깔끔/감각(호기심)
        if (subCategories.contains(PlaceSubCategory.SCIENCE_MUSEUM)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "논리적인", 3,
                            "깔끔한", 1,
                            "감각적인", 1
                    )
            );
        }

        // 미술관/박물관 세분: I/N + 감각/조용/감성
        if (hasAny(subCategories, PlaceSubCategory.ART_MUSEUM, PlaceSubCategory.MUSEUM_GENERAL)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "감각적인", 2,
                            "조용한", 1,
                            "감성적인", 1
                    )
            );
        }

        // 공연장/연극극장: N/F + 분위기/감성/로맨틱
        if (subCategories.contains(PlaceSubCategory.THEATER)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "분위기좋은", 2,
                            "감성적인", 2,
                            "로맨틱한", 1
                    )
            );
        }

        // 문화원: J/N + 계획/조용/감성
        if (subCategories.contains(PlaceSubCategory.CULTURAL_CENTER)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "계획적인", 2,
                            "조용한", 1,
                            "감성적인", 1
                    )
            );
        }

        // 스포츠레저(총괄): E/P + 활동/즉흥
        if (subCategories.contains(PlaceSubCategory.SPORTS_LEISURE)) {
            boost(keywordWeights, inferredKeywords,
                    Map.of(
                            "활동적인", 3,
                            "즉흥적인", 2
                    )
            );
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
    private void boost(
            Map<String, Integer> result,
            List<String> inferred,
            Map<String, Integer> rule
    ) {
        for (Map.Entry<String, Integer> e : rule.entrySet()) {
            if (inferred.contains(e.getKey())) {
                int MAX = 3;

                result.merge(
                        e.getKey(),
                        e.getValue(),
                        (oldV, newV) -> Math.min(oldV + newV, MAX)
                );
            }
        }
    }

    private boolean hasAny(Set<PlaceSubCategory> set, PlaceSubCategory... targets) {
        for (PlaceSubCategory t : targets) {
            if (set.contains(t)) return true;
        }
        return false;
    }

    public void adjustWeights(
            Place place,
            Map<Keyword, Integer> adjustments
    ) {
        for (PlaceKeyword pk : place.getPlaceKeywords()) {
            Integer delta = adjustments.get(pk.getKeyword());
            if (delta == null) continue;

            pk.increaseWeight(delta);
        }
    }
}

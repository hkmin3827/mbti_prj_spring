package com.whatslovermbti.mbti_prj.service.place;

import com.whatslovermbti.mbti_prj.constant.PlaceSubCategory;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;

@Component
public class SubCategoryResolver {

    public Set<PlaceSubCategory> resolveFromCategoryName(String categoryName) {

        Set<PlaceSubCategory> result = EnumSet.noneOf(PlaceSubCategory.class);

        if (categoryName == null || categoryName.isBlank()) {
            return result;
        }
        String t = categoryName;

        if (contains(t, "레스토랑")) {
            result.add(PlaceSubCategory.RESTAURANT);
        }

        if (contains(t, "파인다이닝", "오마카세")) {
            result.add(PlaceSubCategory.FINE_DINING);
        }

        if (contains(t, "술집", "주점")) {
            result.add(PlaceSubCategory.BAR);
        }

        if (contains(t, "이자카야")) {
            result.add(PlaceSubCategory.IZAKAYA);
            result.add(PlaceSubCategory.JAPANESE_PUB);
        }

        if (contains(t, "포장마차")) {
            result.add(PlaceSubCategory.POCHA);
        }

        if (contains(t, "호프", "펍")) {
            result.add(PlaceSubCategory.PUB);
        }

        if (contains(t, "뷔페")) {
            result.add(PlaceSubCategory.BUFFET);
        }

        if (contains(t, "패스트푸드", "햄버거")) {
            result.add(PlaceSubCategory.FAST_CASUAL);
        }

        if (contains(t, "분식", "백반", "국밥", "김밥")) {
            result.add(PlaceSubCategory.QUICK_MEAL);
        }


        if (contains(t, "한식")) result.add(PlaceSubCategory.KOREAN);
        if (contains(t, "일식")) result.add(PlaceSubCategory.JAPANESE);
        if (contains(t, "양식")) result.add(PlaceSubCategory.WESTERN);
        if (contains(t, "중식")) result.add(PlaceSubCategory.CHINESE);

        if (contains(t, "아시아", "동남아", "태국", "베트남", "인도", "인도네시아")) {
            result.add(PlaceSubCategory.ASIAN_FOOD);
        }

        if (contains(t, "철판")) result.add(PlaceSubCategory.TEPPANYAKI);
        if (contains(t, "샤브", "샤브샤브")) result.add(PlaceSubCategory.SHABU_SHABU);
        if (contains(t, "치킨")) result.add(PlaceSubCategory.CHICKEN);
        if (contains(t, "샐러드")) result.add(PlaceSubCategory.SALAD);
        if (contains(t, "도시락")) result.add(PlaceSubCategory.LUNCHBOX);


        if (contains(t, "간식", "토스트", "핫도그", "붕어빵", "떡볶이")) {
            result.add(PlaceSubCategory.SNACK);
        }

        if (contains(t, "푸드코트")) result.add(PlaceSubCategory.FOOD_COURT);
        if (contains(t, "오뎅바", "어묵바")) result.add(PlaceSubCategory.ODEN_BAR);

        if (contains(t, "칵테일바")) {
            result.add(PlaceSubCategory.COCKTAIL_BAR);
            result.add(PlaceSubCategory.BAR);
        }

        if (contains(t, "와인바")) {
            result.add(PlaceSubCategory.WINE_BAR);
            result.add(PlaceSubCategory.BAR);
        }

        if (contains(t, "요리주점")) {
            result.add(PlaceSubCategory.GASTRO_PUB);
            result.add(PlaceSubCategory.BAR);
        }

        if (contains(t, "실내포장마차")) {
            result.add(PlaceSubCategory.INDOOR_POCHA);
            result.add(PlaceSubCategory.POCHA);
        }

        if (contains(t, "일본식주점")) {
            result.add(PlaceSubCategory.JAPANESE_PUB);
            result.add(PlaceSubCategory.IZAKAYA);
        }


        if (contains(t, "베이커리")) {
            result.add(PlaceSubCategory.BAKERY);
        }

        if (contains(t, "디저트")) {
            result.add(PlaceSubCategory.DESSERT);
        }

        if (contains(t, "스터디카페")) {
            result.add(PlaceSubCategory.STUDY_CAFE);
        }

        if (contains(t, "북카페")) {
            result.add(PlaceSubCategory.BOOK_CAFE);
            result.add(PlaceSubCategory.STUDY_CAFE);
        }

        if (contains(t, "루프탑")) {
            result.add(PlaceSubCategory.ROOFTOP);
        }

        if (contains(t, "테라스")) {
            result.add(PlaceSubCategory.TERRACE);
        }


        if (contains(t, "키즈카페")) result.add(PlaceSubCategory.KIDS_CAFE);
        if (contains(t, "전통찻집", "전통차", "찻집")) result.add(PlaceSubCategory.TRADITIONAL_TEA_HOUSE);
        if (contains(t, "디저트카페")) result.add(PlaceSubCategory.DESSERT_CAFE);
        if (contains(t, "무인카페")) result.add(PlaceSubCategory.UNMANNED_CAFE);
        if (contains(t, "생과일", "과일전문")) result.add(PlaceSubCategory.FRESH_FRUIT_SHOP);
        if (contains(t, "갤러리카페", "갤러리")) result.add(PlaceSubCategory.GALLERY_CAFE);
        if (contains(t, "애견카페", "반려견카페")) result.add(PlaceSubCategory.PET_CAFE);
        if (contains(t, "라이브카페")) result.add(PlaceSubCategory.LIVE_CAFE);
        if (contains(t, "보드카페", "보드게임카페")) result.add(PlaceSubCategory.BOARD_CAFE);
        if (contains(t, "다방")) result.add(PlaceSubCategory.DABANG);
        if (contains(t, "카페거리")) result.add(PlaceSubCategory.CAFE_STREET);

        if (contains(t, "공원")) {
            result.add(PlaceSubCategory.PARK);
        }

        if (contains(t, "산책", "둘레길")) {
            result.add(PlaceSubCategory.WALK);
        }

        if (contains(t, "전망", "전망대")) {
            result.add(PlaceSubCategory.VIEW);
            result.add(PlaceSubCategory.OBSERVATORY);
        }

        if (contains(t, "미술관", "박물관")) {
            result.add(PlaceSubCategory.MUSEUM);
            result.add(PlaceSubCategory.CULTURE);
        }

        if (contains(t, "전시관", "갤러리")) {
            result.add(PlaceSubCategory.EXHIBITION);
            result.add(PlaceSubCategory.CULTURE);
        }

        if (contains(t, "공연장", "콘서트")) {
            result.add(PlaceSubCategory.PERFORMANCE);
            result.add(PlaceSubCategory.CULTURE);
        }

        if (contains(t, "영화관", "극장")) {
            result.add(PlaceSubCategory.CINEMA);
            result.add(PlaceSubCategory.CULTURE);
        }

        if (contains(t, "체험", "레저", "놀이시설")) {
            result.add(PlaceSubCategory.ACTIVITY);
        }

        if (contains(t, "보드게임", "방탈출", "VR")) {
            result.add(PlaceSubCategory.GAME);
            result.add(PlaceSubCategory.ACTIVITY);
        }

        if (contains(t, "스포츠", "볼링장", "당구장")) {
            result.add(PlaceSubCategory.SPORTS);
            result.add(PlaceSubCategory.ACTIVITY);
            result.add(PlaceSubCategory.SPORTS_LEISURE);
        }

        if (contains(t, "먹자골목")) result.add(PlaceSubCategory.FOOD_ALLEY);
        if (contains(t, "테마파크")) result.add(PlaceSubCategory.THEME_PARK);
        if (contains(t, "도보여행")) result.add(PlaceSubCategory.WALKING_TOUR);
        if (contains(t, "수목원", "식물원")) result.add(PlaceSubCategory.ARBORETUM);
        if (contains(t, "숲")) result.add(PlaceSubCategory.FOREST);
        if (contains(t, "산")) result.add(PlaceSubCategory.MOUNTAIN);

        if (contains(t, "미술관")) result.add(PlaceSubCategory.ART_MUSEUM);
        if (contains(t, "공연장", "연극", "극장")) result.add(PlaceSubCategory.THEATER);
        if (contains(t, "과학관")) result.add(PlaceSubCategory.SCIENCE_MUSEUM);
        if (contains(t, "박물관")) result.add(PlaceSubCategory.MUSEUM_GENERAL);
        if (contains(t, "기념관")) result.add(PlaceSubCategory.MEMORIAL_HALL);
        if (contains(t, "도자기", "도예촌")) result.add(PlaceSubCategory.POTTERY_VILLAGE);

        if (contains(t, "저수지")) result.add(PlaceSubCategory.RESERVOIR);
        if (contains(t, "온천")) result.add(PlaceSubCategory.HOT_SPRING);
        if (contains(t, "자전거")) result.add(PlaceSubCategory.BIKE_TRIP);
        if (contains(t, "유원지")) result.add(PlaceSubCategory.AMUSEMENT_PARK);

        if (contains(t, "해수욕장", "해변")) result.add(PlaceSubCategory.BEACH);
        if (contains(t, "섬")) result.add(PlaceSubCategory.ISLAND);
        if (contains(t, "문화유적", "유적")) result.add(PlaceSubCategory.CULTURAL_HERITAGE);
        if (contains(t, "계곡")) result.add(PlaceSubCategory.VALLEY);
        if (contains(t, "아쿠아리움")) result.add(PlaceSubCategory.AQUARIUM);
        if (contains(t, "문화원")) result.add(PlaceSubCategory.CULTURAL_CENTER);

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
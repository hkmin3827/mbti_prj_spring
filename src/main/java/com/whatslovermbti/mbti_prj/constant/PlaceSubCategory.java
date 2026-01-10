package com.whatslovermbti.mbti_prj.constant;


public enum PlaceSubCategory {

    /* ===================== FD6 : 음식점 (기존 유지) ===================== */
    RESTAURANT,
    FINE_DINING,
    BAR,
    IZAKAYA,
    POCHA,
    PUB,
    BUFFET,
    FAST_CASUAL,
    QUICK_MEAL,

    /* ===================== FD6 : 음식점 (추가) ===================== */
    KOREAN,              // 한식
    JAPANESE,            // 일식
    WESTERN,             // 양식
    CHINESE,             // 중식
    ASIAN_FOOD,          // 아시아음식(동남아/인도/etc)
    TEPPANYAKI,          // 철판요리
    SHABU_SHABU,         // 샤브샤브
    CHICKEN,             // 치킨
    SALAD,               // 샐러드(샐러드 전문)
    LUNCHBOX,            // 도시락
    SNACK,               // 간식(길거리/간편식 성격)
    FOOD_COURT,          // 푸드코트
    ODEN_BAR,            // 오뎅바
    COCKTAIL_BAR,        // 칵테일바
    WINE_BAR,            // 와인바
    GASTRO_PUB,          // 요리주점(안주/요리 중심)
    INDOOR_POCHA,        // 실내포장마차
    JAPANESE_PUB,        // 일본식주점(이자카야 범주를 좀 더 넓게)

    /* ===================== CE7 : 카페 (기존 유지) ===================== */
    BAKERY,
    DESSERT,
    ROOFTOP,
    TERRACE,
    STUDY_CAFE,

    /* ===================== CE7 : 카페 (추가) ===================== */
    KIDS_CAFE,               // 키즈카페
    TRADITIONAL_TEA_HOUSE,   // 전통찻집
    DESSERT_CAFE,            // 디저트카페(카페+디저트 성격)
    UNMANNED_CAFE,           // 무인카페
    FRESH_FRUIT_SHOP,        // 생과일전문점
    BOOK_CAFE,               // 북카페(스터디카페와 구분)
    GALLERY_CAFE,            // 갤러리카페
    PET_CAFE,                // 애견카페
    LIVE_CAFE,               // 라이브카페
    BOARD_CAFE,              // 보드카페(보드게임카페)
    DABANG,                  // 다방(레트로)
    CAFE_STREET,             // 카페거리(코스에도 포함되지만 성격상 카페로도 분류)

    /* ===================== COURSE : (기존 유지) ===================== */
    PARK,
    WALK,
    VIEW,
    EXHIBITION,
    MUSEUM,
    ACTIVITY,
    GAME,
    SPORTS,
    CULTURE,
    PERFORMANCE,
    CINEMA,

    /* ===================== COURSE : (추가) ===================== */
    FOOD_ALLEY,          // 먹자골목
    THEME_PARK,          // 테마파크
    WALKING_TOUR,        // 도보여행
    ARBORETUM,           // 수목원/식물원
    FOREST,              // 숲
    OBSERVATORY,         // 전망대(=VIEW 세분)
    MOUNTAIN,            // 산
    ART_MUSEUM,          // 미술관(=MUSEUM 세분)
    THEATER,             // 공연장/연극극장(=PERFORMANCE 세분)
    SCIENCE_MUSEUM,      // 과학관
    MUSEUM_GENERAL,      // 박물관(=MUSEUM 세분)
    MEMORIAL_HALL,       // 기념관
    POTTERY_VILLAGE,     // 도자기/도예촌
    RESERVOIR,           // 저수지
    HOT_SPRING,          // 온천
    BIKE_TRIP,           // 자전거여행
    AMUSEMENT_PARK,      // 유원지
    BEACH,               // 해수욕장/해변
    ISLAND,              // 섬
    CULTURAL_HERITAGE,   // 문화유적
    VALLEY,              // 계곡
    AQUARIUM,            // 아쿠아리움
    CULTURAL_CENTER,     // 문화원

    /* ===================== 스포츠/레저 (추가) ===================== */
    SPORTS_LEISURE       // 스포츠레저(총괄)
}
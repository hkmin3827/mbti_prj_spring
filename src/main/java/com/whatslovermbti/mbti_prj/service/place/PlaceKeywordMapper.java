package com.whatslovermbti.mbti_prj.service.place;

import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.PlaceKeyword;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
import com.whatslovermbti.mbti_prj.repository.KeywordRepository;
import com.whatslovermbti.mbti_prj.repository.PlaceKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceKeywordMapper {

    private final KeywordRepository keywordRepository;
    private final PlaceKeywordRepository placeKeywordRepository;

    public void mapKeywords(Place place, KakaoMapResponse.Document d) {

        String placeName = d.getPlaceName();
        String category = d.getCategoryName(); // ex) "카페 > 디저트 > 커피"

        List<String> keywordNames = new ArrayList<>();

        /* =========================
           ☕ 카페 계열
           ========================= */
        if (category.contains("카페")) {

            // 카페 + 스터디 / 북카페
            if (containsAny(placeName, "스터디", "북카페", "라이브러리")) {
                keywordNames.add("조용한");
                keywordNames.add("논리적인");
            }

            // 카페 + 루프탑 / 테라스 / 전망
            if (containsAny(placeName, "루프탑", "테라스", "전망")) {
                keywordNames.add("로맨틱한");
            }

            // 카페 + 라운지 / 갤러리
            if (containsAny(placeName, "라운지", "갤러리")) {
                keywordNames.add("감성적인");
            }

            // 카페 + 파티 / 클럽
            if (containsAny(placeName, "파티", "클럽")) {
                keywordNames.add("시끌벅적한");
                keywordNames.add("활동적인");
            }
        }

        /* =========================
           🍽️ 음식점 계열
           ========================= */
        if (category.contains("음식점") || category.contains("레스토랑")) {

            if (containsAny(placeName, "와인", "비스트로", "파인다이닝", "오마카세")) {
                keywordNames.add("로맨틱한");
                keywordNames.add("계획적인");
            }

            if (containsAny(placeName, "뷔페", "무한")) {
                keywordNames.add("활동적인");
            }
        }

        /* =========================
           🎡 관광명소 / 데이트
           ========================= */
        if (category.contains("관광") || category.contains("전시") || category.contains("미술관")) {

            if (containsAny(placeName, "전시", "미술", "갤러리")) {
                keywordNames.add("감성적인");
                keywordNames.add("논리적인");
            }

            if (containsAny(placeName, "공원", "산책", "전망")) {
                keywordNames.add("자유로운");
                keywordNames.add("즉흥적인");
            }
        }

        /* =========================
           저장 (내부 표준 키워드만)
           ========================= */
        for (String name : keywordNames) {
            keywordRepository.findByName(name).ifPresent(keyword -> {
                if (!placeKeywordRepository.existsByPlaceAndKeyword(place, keyword)) {
                    placeKeywordRepository.save(new PlaceKeyword(place, keyword));
                }
            });
        }
    }

    private boolean containsAny(String target, String... keywords) {
        if (target == null) return false;
        for (String k : keywords) {
            if (target.contains(k)) return true;
        }
        return false;
    }
}

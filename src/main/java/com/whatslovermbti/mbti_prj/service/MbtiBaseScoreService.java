//package com.whatslovermbti.mbti_prj.service;
//
//import com.whatslovermbti.mbti_prj.entity.*;
//import com.whatslovermbti.mbti_prj.repository.MbtiKeywordWeightRepository;
//import com.whatslovermbti.mbti_prj.util.MbtiAxisUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class MbtiBaseScoreService {
//
//    private final MbtiKeywordWeightRepository mbtiKeywordWeightRepository;
//
//    /**
//     * MBTI 기반 선천 점수 계산
//     */
//    public int calculateBaseScore(User user, Place place) {
//
//        String mbti = user.getMbti(); // ex) "INTJ"
//        Map<String, Integer> mbtiAxes = MbtiAxisUtil.parseAxes(mbti);
//
//        if (mbtiAxes.isEmpty()) {
//            return 0;
//        }
//
//        // 사용자의 MBTI 축 (I, N, T, J 등)
//        Set<String> axes = mbtiAxes.keySet();
//
//        // 해당 축들에 대한 키워드 가중치 전부 조회
//        List<MbtiKeywordWeight> mbtiWeights =
//                mbtiKeywordWeightRepository.findByMbtiAxisIn(axes);
//
//        int totalScore = 0;
//
//        for (PlaceKeyword pk : place.getPlaceKeywords()) {
//
//            Keyword keyword = pk.getKeyword();
//            int placeWeight = pk.getWeight(); // 장소-키워드 강도
//
//            int mbtiKeywordScore = mbtiWeights.stream()
//                    .filter(w -> w.getKeyword().getId().equals(keyword.getId()))
//                    .mapToInt(MbtiKeywordWeight::getWeight)
//                    .sum();
//
//            totalScore += placeWeight * mbtiKeywordScore;
//            //totalScore > 0
//            //MBTI 성향에 잘 맞는 장소
//
//            //totalScore < 0
//            //성향상 안 맞는 장소
//        }
//        return totalScore;
//    }
//}

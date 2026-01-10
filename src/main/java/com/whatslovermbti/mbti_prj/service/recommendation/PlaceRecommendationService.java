package com.whatslovermbti.mbti_prj.service.recommendation;

import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.constant.PlaceSubCategory;
import com.whatslovermbti.mbti_prj.dto.place.PlaceSnapshot;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoCategoryMapper;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
import com.whatslovermbti.mbti_prj.resolver.context.TargetMbtiResolver;
import com.whatslovermbti.mbti_prj.service.place.SubCategoryResolver;
import com.whatslovermbti.mbti_prj.service.DocumentKeywordInferer;
import com.whatslovermbti.mbti_prj.service.MbtiScoreCalculator;
import com.whatslovermbti.mbti_prj.service.keyword.KeywordWeightAggregator;
import com.whatslovermbti.mbti_prj.util.RandomPicker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Target;
import java.util.*;

// 유저에게 이 후보군 중 무엇을, 어떤 비율로 보여줄 것인가
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceRecommendationService {

    // 상위 몇 개를 “우선 노출 풀”로 볼 것인가
    private static final int TOP_RANGE = 45;

    private final KeywordWeightAggregator keywordWeightAggregator;
    private final MbtiScoreCalculator mbtiScoreCalculator;
    private final DocumentKeywordInferer keywordInferer;
    private final SubCategoryResolver subCategoryResolver;
    private final TargetMbtiResolver mbtiResolver;

    // 핵심 추천 엔진 - 후보군은 확보 되어있고, score로 개인화 처리
    /*
     * 규칙:
     * - DB write 절대 금지
     * - Kakao API 접근 금지
     */
    public List<KakaoMapResponse.Document> recommendFromCandidates(
            User user,
            MbtiContext context,
            List<KakaoMapResponse.Document> candidates,
            int limit
    ) {
        if (candidates == null || candidates.isEmpty()) {
            return List.of();
        }

        // 키워드 기반 가중치 Map (String 키워드)
        Map<String, Integer> keywordWeightMap =
                keywordWeightAggregator.getCombinedKeywordWeightMapByName(user, mbtiResolver.resolve(user, context));


        List<ScoredDoc> scored =
                candidates.stream()
                        .map(doc -> {
                            Set<PlaceSubCategory> sub =
                                    subCategoryResolver.resolveFromCategoryName(doc.getCategoryName());

                            PlaceSnapshot snapshot = PlaceSnapshot.from(doc);

                            List<String> inferred =
                                    keywordInferer.infer(snapshot, sub);

                            int score =
                                    mbtiScoreCalculator.calculateDocumentScore(
                                            KakaoCategoryMapper.resolveCategory(doc.getCategoryGroupCode()),
                                            inferred,
                                            keywordWeightMap
                                    );

                            if (score > 0) {
                                log.info(
                                        "[DOC_SCORE] placeName={}, keywords={}, score={}",
                                        doc.getPlaceName(),
                                        inferred,
                                        score
                                );
                            }

                            return new ScoredDoc(doc, score, inferred);
                        })
                        .toList();

        // RandomPicker에게 전부 위임
        List<KakaoMapResponse.Document> result =
                RandomPicker.pickWithBias(
                        scored.stream()
                                .map(ScoredDoc::doc)
                                .toList(),
                        TOP_RANGE,
                        limit
                );


        log.info(
                "[PICK_RESULT] total={}, first={}",
                result.size(),
                result.isEmpty() ? null : result.get(0).getPlaceName()
        );

        return result;
    }
}
record ScoredDoc(
        KakaoMapResponse.Document doc,
        int score,
        List<String> keywords
) {}
package com.whatslovermbti.mbti_prj.service.recommendation;

import com.whatslovermbti.mbti_prj.constant.Category;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.constant.PlaceSubCategory;
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

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceRecommendationService {

    private static final int TOP_RANGE = 45;

    private final KeywordWeightAggregator keywordWeightAggregator;
    private final MbtiScoreCalculator mbtiScoreCalculator;
    private final DocumentKeywordInferer keywordInferer;
    private final SubCategoryResolver subCategoryResolver;
    private final TargetMbtiResolver mbtiResolver;
    private final UserPreferenceScoreApplier userPreferenceScoreApplier;


    public List<KakaoMapResponse.Document> recommendFromCandidates(
            User user,
            MbtiContext context,
            List<KakaoMapResponse.Document> candidates,
            int limit
    ) {
        if (candidates == null || candidates.isEmpty()) {
            return List.of();
        }
        Map<String, Double> combinedWeightMap =
                keywordWeightAggregator.getCombinedKeywordWeightMapByName(user,  mbtiResolver.resolve(user, context));

        Map<String, Double> userPrefMap =
                keywordWeightAggregator.getUserKeywordPreferenceMapByName(user);


        List<ScoredDoc> scored =
                candidates.stream()
                        .map(doc -> {
                            Set<PlaceSubCategory> sub =
                                    subCategoryResolver.resolveFromCategoryName(doc.getCategoryName());
                            Category cate = KakaoCategoryMapper.resolveCategory(doc.getCategoryGroupCode());

                            List<String> inferred =
                                    keywordInferer.infer(sub, cate);


                            double baseScore =
                                    mbtiScoreCalculator.calculateDocumentScore(
                                            inferred,
                                            combinedWeightMap
                                    );


                            double userBonus =
                                    userPreferenceScoreApplier.apply(userPrefMap, inferred);

                            double score = baseScore + userBonus;

                                log.info(
                                        "[DOC_SCORE] placeName={}, sub={}, keywords={}, score={}",
                                        doc.getPlaceName(),
                                        sub,
                                        inferred,
                                        score
                                );

                            return new ScoredDoc(doc, score, inferred);
                        })
                        .sorted(Comparator.comparingDouble(ScoredDoc::score).reversed())
                        .toList();


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
        double score,
        List<String> keywords
) {}
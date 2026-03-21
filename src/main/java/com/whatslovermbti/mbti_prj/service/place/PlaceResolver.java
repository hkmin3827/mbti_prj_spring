package com.whatslovermbti.mbti_prj.service.place;

import com.whatslovermbti.mbti_prj.constant.Category;
import com.whatslovermbti.mbti_prj.constant.PlaceSubCategory;
import com.whatslovermbti.mbti_prj.dto.place.PlaceSnapshot;
import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.PlaceKeyword;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoCategoryMapper;
import com.whatslovermbti.mbti_prj.repository.PlaceKeywordRepository;
import com.whatslovermbti.mbti_prj.repository.PlaceRepository;
import com.whatslovermbti.mbti_prj.service.DocumentKeywordInferer;
import com.whatslovermbti.mbti_prj.service.llm.GeminiService;
import com.whatslovermbti.mbti_prj.service.llm.PlaceKeywordAdjustmentCalculator;
import com.whatslovermbti.mbti_prj.service.llm.dto.PlaceAtmosphereResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceResolver {

    private final PlaceRepository placeRepository;
    private final PlaceKeywordMapper placeKeywordMapper;
    private final SubCategoryResolver subCategoryResolver;
    private final DocumentKeywordInferer documentKeywordInferer;
    private final PlaceKeywordAdjustmentCalculator placeKeywordAdjustmentCalculator;
    private final GeminiService geminiService;
    private final ApplicationEventPublisher eventPublisher;
    private final PlaceKeywordRepository placeKeywordRepository;

    @Transactional
    public Place resolveAndEnsureKeywords(PlaceSnapshot snapshot) {
        if (snapshot.kakaoPlaceId() == null) {
            throw new IllegalStateException("kakaoPlaceId is null");
        }

        Place place = placeRepository.findByKakaoPlaceId(snapshot.kakaoPlaceId())
                .orElseGet(() -> create(snapshot));

        boolean exists =
                placeKeywordRepository.existsByPlaceId(place.getId());

        if (!exists) {
            initializeKeywords(place, snapshot);
        }

        return place;
    }
    @Transactional
    private Place create(PlaceSnapshot s) {
        Place place = new Place();
        place.setKakaoPlaceId(s.kakaoPlaceId());
        place.setName(s.name());
        place.setLatitude(s.latitude());
        place.setLongitude(s.longitude());
        place.setAddress(s.address());
        place.setRoadAddress(s.roadAddress());
        place.setTelnum(s.phone());
        place.setDescription(s.categoryName());

        Category category =
                KakaoCategoryMapper.resolveCategory(s.categoryGroupCode());

        if (category == Category.COURSE) {
            category = KakaoCategoryMapper.resolveFromCategoryName(s.categoryName());
        }
        place.setCategory(
               category
        );

        Place saved = placeRepository.save(place);
        log.info(
                "[CREATE] kakaoId={}, rawGroupCode='{}'",
                s.kakaoPlaceId(),
                s.categoryGroupCode()
        );log.info(
                "[CREATE] resolvedCategory={}",
                category
        );
        initializeKeywords(saved, s);

        return saved;
    }
    @Transactional
    public void initializeKeywords(Place place, PlaceSnapshot s) {

        Set<PlaceSubCategory> subCategories =
                subCategoryResolver.resolveFromCategoryName(s.categoryName());
        Category cate = KakaoCategoryMapper.resolveCategory(s.categoryGroupCode());

        List<String> inferredKeywords =
                documentKeywordInferer.infer(subCategories, cate);

        placeKeywordMapper.mapInitialKeywords(place, subCategories, inferredKeywords);

        // 기존 place도 AI 보정 트리거
        eventPublisher.publishEvent(
                new PlaceCreatedEvent(place.getId())
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePlaceCreated(PlaceCreatedEvent event) {
        try {
            placeRepository.findById(event.placeId())
                    .ifPresent(this::adjustKeywordsByAI);
        } catch (Exception e) {
            log.warn("Post-commit AI adjustment skipped. placeId={}",
                    event.placeId(), e);
        }
    }

    @Transactional(propagation = REQUIRES_NEW)
    public void adjustKeywordsByAI(Place place) {
        // 보정 전 값 스냅샷
        Map<String, Integer> before =
                place.getPlaceKeywords().stream()
                        .collect(Collectors.toMap(
                                pk -> pk.getKeyword().getName(),
                                PlaceKeyword::getWeight,
                                (a, b) -> a,
                                LinkedHashMap::new
                        ));

        try{
        PlaceAtmosphereResult atmosphere =
                geminiService.analyzePlaceAtmosphere(
                        place.getName(),
                        place.getAddress(),
                        place.getCategory().name()
                );


        log.info("[AI RAW RESPONSE] placeId={}, rawKeywords={}",
                place.getId(),
                atmosphere.rawKeywords());

        Map<Keyword, Integer> adjustments =
                placeKeywordAdjustmentCalculator.calculate(atmosphere);

        placeKeywordMapper.adjustWeights(place, adjustments);
        } catch (WebClientResponseException.TooManyRequests e) {
            log.warn("Gemini 429 skip. placeId={}", place.getId());
        } catch (WebClientResponseException.ServiceUnavailable e) {
            log.warn("Gemini 503 skip. placeId={}", place.getId());
        } catch (Exception e) {
            log.error("Gemini unexpected error. placeId={}", place.getId(), e);
        }

        // 보정 후 값 스냅샷
        Map<String, Integer> after =
                place.getPlaceKeywords().stream()
                        .collect(Collectors.toMap(
                                pk -> pk.getKeyword().getName(),
                                PlaceKeyword::getWeight,
                                (a, b) -> a,
                                LinkedHashMap::new
                        ));

        log.info("AI keyword adjustment - placeId={}, name={}",
                place.getId(), place.getName());
        before.forEach((k, v) -> {
            int a = after.getOrDefault(k, v);
            if (v != a) {
                log.info("  [{}] {} -> {}", k, v, a);
            }
        });

    }

    public record PlaceCreatedEvent(Long placeId) {}
}


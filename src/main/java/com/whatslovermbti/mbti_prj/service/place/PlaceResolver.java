    package com.whatslovermbti.mbti_prj.service.place;

    import com.whatslovermbti.mbti_prj.constant.PlaceSubCategory;
    import com.whatslovermbti.mbti_prj.dto.place.PlaceSnapshot;
    import com.whatslovermbti.mbti_prj.entity.Keyword;
    import com.whatslovermbti.mbti_prj.entity.Place;
    import com.whatslovermbti.mbti_prj.entity.PlaceKeyword;
    import com.whatslovermbti.mbti_prj.infra.kakao.KakaoCategoryMapper;
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

        @Transactional
        public Place resolve(PlaceSnapshot snapshot) {
            if (snapshot.kakaoPlaceId() == null) {
                throw new IllegalStateException("kakaoPlaceId is null");
            }

            return placeRepository.findByKakaoPlaceId(snapshot.kakaoPlaceId())
                    .orElseGet(() -> create(snapshot));
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
            place.setCategory(
                    KakaoCategoryMapper.resolveCategory(s.categoryName())
            );

            Place saved = placeRepository.save(place);

            Set<PlaceSubCategory> subCategories =
                    subCategoryResolver.resolveFromCategoryName(s.categoryName());

            List<String> inferredKeywords =
                    documentKeywordInferer.infer(s, subCategories);

            placeKeywordMapper.mapKeywords(saved, subCategories, inferredKeywords);

            // 여기서 이벤트만 발행 (AI 호출 X)
            eventPublisher.publishEvent(
                    new PlaceCreatedEvent(saved.getId())
            );


            return saved;
        }

        // 커밋이 끝난 뒤 Spring이 자동 호출
        @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
        public void handlePlaceCreated(PlaceCreatedEvent event) {

            placeRepository.findById(event.placeId())
                    .ifPresent(this::adjustKeywordsByAI);
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

            // 보정 후 값 스냅샷
            Map<String, Integer> after =
                    place.getPlaceKeywords().stream()
                            .collect(Collectors.toMap(
                                    pk -> pk.getKeyword().getName(),
                                    PlaceKeyword::getWeight,
                                    (a, b) -> a,
                                    LinkedHashMap::new
                            ));

            // diff 로그
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


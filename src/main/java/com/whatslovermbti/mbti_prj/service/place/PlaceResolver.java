package com.whatslovermbti.mbti_prj.service.place;

import com.whatslovermbti.mbti_prj.constant.PlaceSubCategory;
import com.whatslovermbti.mbti_prj.dto.place.PlaceSnapshot;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoCategoryMapper;
import com.whatslovermbti.mbti_prj.repository.PlaceRepository;
import com.whatslovermbti.mbti_prj.service.DocumentKeywordInferer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceResolver {

    private final PlaceRepository placeRepository;
    private final PlaceKeywordMapper placeKeywordMapper;
    private final SubCategoryResolver subCategoryResolver;
    private final DocumentKeywordInferer documentKeywordInferer;

    public Place resolve(PlaceSnapshot snapshot) {
        if (snapshot.kakaoPlaceId() == null) {
            throw new IllegalStateException("kakaoPlaceId is null");
        }

        return placeRepository.findByKakaoPlaceId(snapshot.kakaoPlaceId())
                .orElseGet(() -> create(snapshot));
    }

    private Place create(PlaceSnapshot s) {
        Place place = new Place();
        place.setKakaoPlaceId(s.kakaoPlaceId());
        place.setName(s.name());
        place.setLatitude(s.latitude());
        place.setLongitude(s.longitude());
        place.setCategory(
                KakaoCategoryMapper.resolveCategory(s.categoryName())
        );

        Place saved = placeRepository.save(place);

        Set<PlaceSubCategory> subCategories =
                subCategoryResolver.resolveFromCategoryName(s.categoryName());

        List<String> inferredKeywords =
                documentKeywordInferer.infer(s, subCategories);

        placeKeywordMapper.mapKeywords(saved, subCategories, inferredKeywords);

        return saved;
    }
}
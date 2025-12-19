package com.whatslovermbti.mbti_prj.service.place;

import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
import com.whatslovermbti.mbti_prj.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlacePersistenceService {
    private final PlaceRepository placeRepository;

    public Place getOrCreate(KakaoMapResponse.Document d) {

        return placeRepository.findByMapPlaceId(d.getId())
                .orElseGet(() -> {
                    Place p = new Place();
                    p.setName(d.getPlaceName());
                    p.setAddress(d.getAddressName());
                    p.setLatitude(Double.valueOf(d.getLatitude()));
                    p.setLongitude(Double.valueOf(d.getLongitude()));
                    p.setMapPlaceId(d.getId());
                    return placeRepository.save(p);
                });
    }
}

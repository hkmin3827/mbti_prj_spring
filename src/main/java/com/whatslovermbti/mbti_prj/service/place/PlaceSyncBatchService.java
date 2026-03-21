package com.whatslovermbti.mbti_prj.service.place;

import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.repository.PlaceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceSyncBatchService {

    private final PlaceRepository placeRepository;
    private final PlaceSyncService placeSyncService;

    @Scheduled(cron = "0 0 4 * * *")
    @Transactional
    public void syncPlaces() {

        List<Place> activePlaces =
                placeRepository.findAllByDeletedFalse();

        for (Place place : activePlaces) {
            placeSyncService.validatePlace(place);
        }

        log.info("Place sync batch completed");
    }
}
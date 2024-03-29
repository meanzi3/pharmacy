package com.example.pharmacy.direction.service;

import com.example.pharmacy.api.dto.DocumentDto;
import com.example.pharmacy.api.service.KakaoCategorySearchService;
import com.example.pharmacy.direction.entity.Direction;
import com.example.pharmacy.direction.repository.DirectionRepository;
import com.example.pharmacy.domain.dto.PharmacyDto;
import com.example.pharmacy.domain.service.PharmacySearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.Collection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.MathContext;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectionService {

  private static final int MAX_SEARCH_COUNT = 3; // 약국 최대 검색 갯수
  private static final double RADIUS_KM = 10.0; // 반경 10 km

  private final PharmacySearchService pharmacySearchService;
  private final DirectionRepository directionRepository;
  private final KakaoCategorySearchService kakaoCategorySearchService;
  private final Base62Service base62Service;
  private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";


  @Transactional
  public List<Direction> saveAll(List<Direction> directionList){
    if(CollectionUtils.isEmpty(directionList)) return Collections.emptyList();
    return directionRepository.saveAll(directionList);
  }

  public String findDirectionUrlById(String encodedId){
    Long decodedId = base62Service.decodeDirectionId(encodedId);
    Direction direction = directionRepository.findById(decodedId).orElse(null);

    String params2 = String.join(",",direction.getTargetPharmacyName(),String.valueOf(direction.getTargetLatitude()), String.valueOf(direction.getTargetLongitude()));
    String result2 = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params2).toUriString();

    return result2;

  }

  public List<Direction> buildDirectionList(DocumentDto documentDto) {

    if(Objects.isNull(documentDto)) return Collections.emptyList();

    return pharmacySearchService.searchPharmacyDtoList()
            .stream().map(pharmacyDto ->
                    Direction.builder()
                            .inputAddress(documentDto.getAddressName())
                            .inputLatitude(documentDto.getLatitude())
                            .inputLongitude(documentDto.getLongitude())
                            .targetPharmacyName(pharmacyDto.getPharmacyName())
                            .targetAddress(pharmacyDto.getPharmacyAddress())
                            .targetLatitude(pharmacyDto.getLatitude())
                            .targetLongitude(pharmacyDto.getLongitude())
                            .distance(
                                    calculateDistance(documentDto.getLatitude(), documentDto.getLongitude(),
                                            pharmacyDto.getLatitude(), pharmacyDto.getLongitude())
                            )
                            .build())
            .filter(direction -> direction.getDistance() <= RADIUS_KM)
            .sorted(Comparator.comparing(Direction::getDistance))
            .limit(MAX_SEARCH_COUNT)
            .collect(Collectors.toList());
  }

  public List<Direction> buildDirectionListByCategoryApi(DocumentDto inputDocumentDto) {

    if(Objects.isNull(inputDocumentDto)) return Collections.emptyList();

    return kakaoCategorySearchService
            .requestPharmacyCategorySearch(inputDocumentDto.getLatitude(), inputDocumentDto.getLongitude(), RADIUS_KM)
            .getDocumentList()
            .stream().map(resultDocumentDto ->
                    Direction.builder()
                            .inputAddress(inputDocumentDto.getAddressName())
                            .inputLatitude(inputDocumentDto.getLatitude())
                            .inputLongitude(inputDocumentDto.getLongitude())
                            .targetPharmacyName(resultDocumentDto.getPlaceName())
                            .targetAddress(resultDocumentDto.getAddressName())
                            .targetLatitude(resultDocumentDto.getLatitude())
                            .targetLongitude(resultDocumentDto.getLongitude())
                            .distance(resultDocumentDto.getDistance() * 0.001) // km 단위
                            .build())
            .limit(MAX_SEARCH_COUNT) // 최대 3개
            .collect(Collectors.toList()); // 리스트로
  }


  // Haversine formula => 두 위치 간의 거리를 계산하는 알고리즘
  private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    lat1 = Math.toRadians(lat1);
    lon1 = Math.toRadians(lon1);
    lat2 = Math.toRadians(lat2);
    lon2 = Math.toRadians(lon2);
    double earthRadius = 6371; //Kilometers
    return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
  }
}

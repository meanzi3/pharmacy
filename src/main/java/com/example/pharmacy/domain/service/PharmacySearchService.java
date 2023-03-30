package com.example.pharmacy.domain.service;

import com.example.pharmacy.domain.cache.PharmacyRedisTemplateService;
import com.example.pharmacy.domain.dto.PharmacyDto;
import com.example.pharmacy.domain.entity.Pharmacy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacySearchService {

  private final PharmacyRepositoryService pharmacyRepositoryService;
  private final PharmacyRedisTemplateService pharmacyRedisTemplateService;

  public List<PharmacyDto> searchPharmacyDtoList() {

    // redis 에서 약국 리스트를 검색하고 문제가 있을 시에는 db에서 검색한다.

    // redis
    List<PharmacyDto> pharmacyDtoList = pharmacyRedisTemplateService.findAll();
    if(!pharmacyDtoList.isEmpty()) return pharmacyDtoList;

    // db
    return pharmacyRepositoryService.findAll()
            .stream()
            .map(this::convertToPharmacyDto)
            .collect(Collectors.toList());
  }

  private PharmacyDto convertToPharmacyDto(Pharmacy pharmacy) {

    return PharmacyDto.builder()
            .id(pharmacy.getId())
            .pharmacyAddress(pharmacy.getPharmacyAddress())
            .pharmacyName(pharmacy.getPharmacyName())
            .latitude(pharmacy.getLatitude())
            .longitude(pharmacy.getLongitude())
            .build();
  }
}

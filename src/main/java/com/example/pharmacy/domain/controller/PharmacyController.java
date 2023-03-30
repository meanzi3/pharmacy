package com.example.pharmacy.domain.controller;

import com.example.pharmacy.domain.cache.PharmacyRedisTemplateService;
import com.example.pharmacy.domain.dto.PharmacyDto;
import com.example.pharmacy.domain.service.PharmacyRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PharmacyController {

  private final PharmacyRepositoryService pharmacyRepositoryService;
  private final PharmacyRedisTemplateService pharmacyRedisTemplateService;

  // 데이터 초기 세팅
  @GetMapping("/redis/save")
  public String save(){
    List<PharmacyDto> pharmacyDtoList = pharmacyRepositoryService.findAll()
            .stream().map(pharmacy -> PharmacyDto.builder()
                    .id(pharmacy.getId())
                    .pharmacyName(pharmacy.getPharmacyName())
                    .pharmacyAddress(pharmacy.getPharmacyAddress())
                    .latitude(pharmacy.getLatitude())
                    .longitude(pharmacy.getLongitude())
                    .build())
            .collect(Collectors.toList());

    pharmacyDtoList.forEach(pharmacyRedisTemplateService::save);

    return "success";
  }
}

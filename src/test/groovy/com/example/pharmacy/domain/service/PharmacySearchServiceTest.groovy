package com.example.pharmacy.domain.service

import com.example.pharmacy.domain.cache.PharmacyRedisTemplateService
import com.example.pharmacy.domain.entity.Pharmacy
import com.google.common.collect.Lists
import spock.lang.Specification

class PharmacySearchServiceTest extends Specification {

    private PharmacySearchService pharmacySearchService;

    private PharmacyRepositoryService pharmacyRepositoryService = Mock();
    private PharmacyRedisTemplateService pharmacyRedisTemplateService = Mock();

    private List<Pharmacy> pharmacyList

    def setup(){
        pharmacySearchService = new PharmacySearchService(pharmacyRepositoryService, pharmacyRedisTemplateService);

        pharmacyList = Lists.newArrayList(
                Pharmacy.builder()
                    .id(1L)
                    .pharmacyName("호수온누리약국")
                    .latitude(37.60894036)
                    .longitude(127.029052)
                    .build(),
                Pharmacy.builder()
                    .id(2L)
                    .pharmacyName("돌곶이온누리약국")
                    .latitude(37.61040424)
                    .longitude(127.0569046)
                    .build())
    }

    def "redis에서 오류 발생 시 db를 이용하여 약국 데이터를 조회"(){
        when: // stubbing
        pharmacyRedisTemplateService.findAll() >> []
        pharmacyRepositoryService.findAll() >> pharmacyList

        def result = pharmacySearchService.searchPharmacyDtoList()

        then:
        result.size() == 2
    }
}

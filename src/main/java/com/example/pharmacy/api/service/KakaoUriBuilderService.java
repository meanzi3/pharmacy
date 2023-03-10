package com.example.pharmacy.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
public class KakaoUriBuilderService {
  private static final String KAKAO_LOCAL_SEARCH_ADDRESS_URL = "https://dapi.kakao.com/v2/local/search/address.json";

  public URI buildUrlByAddressSearch(String address){
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_SEARCH_ADDRESS_URL);
    uriBuilder.queryParam("query",address);
    // URI 생성

    URI uri = uriBuilder.build().encode().toUri();
    // 인코딩

    log.info("[KakaoUrlBuilderService buildUrlByAddressSearch] address: {}, uri: {}", address, uri);

    return uri;
  }
}

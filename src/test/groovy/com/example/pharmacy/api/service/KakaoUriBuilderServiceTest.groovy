package com.example.pharmacy.api.service

import spock.lang.Specification

import java.nio.charset.StandardCharsets

class KakaoUriBuilderServiceTest extends Specification {

    private KakaoUriBuilderService kakaoUriBuilderService;

    // run before every feature method
    def setup(){
        kakaoUriBuilderService = new KakaoUriBuilderService();
    }

    def "buildUriByAddressSearch - 한글 파라미터의 경우 정상적으로 인코딩"(){
        given:
        String address = "부산 사상구"
        def charset = StandardCharsets.UTF_8

        when:
        def uri= kakaoUriBuilderService.buildUrlByAddressSearch(address)
        def decodeResult = URLDecoder.decode(uri.toString(), charset)

        then:
        decodeResult == "https://dapi.kakao.com/v2/local/search/address.json?query=부산 사상구"

    }
}

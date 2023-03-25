package com.example.pharmacy.direction.controller;

import com.example.pharmacy.direction.entity.Direction;
import com.example.pharmacy.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@Slf4j
@RequiredArgsConstructor
public class DirectionController {

  private final DirectionService directionService;
  private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";

  @GetMapping("/dir/{encodedId}")
  public String searchDirection(@PathVariable("encodedId") String encodedId){
    Direction resultDirection = directionService.findById(encodedId);

    String params2 = String.join(",",resultDirection.getTargetPharmacyName(),String.valueOf(resultDirection.getTargetLatitude()), String.valueOf(resultDirection.getTargetLongitude()));
    String result2 = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params2).toUriString();
    log.info("direction params: {}, url: {}", params2, result2);

    return "redirect:"+result2;
  }

}

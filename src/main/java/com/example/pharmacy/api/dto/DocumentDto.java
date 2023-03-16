package com.example.pharmacy.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {

  @JsonProperty("place_name")
  private String placeName; // 장소명, 업체명

  @JsonProperty("address_name")
  private String addressName; // 전체 주소

  @JsonProperty("y")
  private double latitude; // 위도

  @JsonProperty("x")
  private double longitude; // 경도

  @JsonProperty("distance")
  private double distance; // 거리
}

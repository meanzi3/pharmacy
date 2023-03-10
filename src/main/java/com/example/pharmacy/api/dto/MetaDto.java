package com.example.pharmacy.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MetaDto {

  @JsonProperty("total_count") // JSON 으로 응답 받을 때 작성된 필드로 매핑시켜줌
  private Integer totalCount;
}

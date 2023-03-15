package com.example.pharmacy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.support.RetryTemplate;

@EnableRetry
@Configuration
public class RetryConfig {

//  @Bean
//  public RetryTemplate retryTemplate(){
//    return new RetryTemplate();
//  }

  // sprig retry 이용 방법에는 두 가지가 있다.
  // 1. 어노테이션 이용 => 이 방법을 이용할 것
  // 2. RetryTemplate 이용
}

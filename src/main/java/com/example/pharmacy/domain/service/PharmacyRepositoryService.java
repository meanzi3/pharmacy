package com.example.pharmacy.domain.service;

import com.example.pharmacy.domain.entity.Pharmacy;
import com.example.pharmacy.domain.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRepositoryService {

  private final PharmacyRepository pharmacyRepository;

  @Transactional
  public void updateAddress(Long id, String address){
    Pharmacy entity =pharmacyRepository.findById(id).orElse(null);

    if(Objects.isNull(entity)){
      log.error("{PharmacyRepositoryService updateAddress} not found id : {}",id);
      return;
    }

    entity.changePharmacyAddress(address);
  }

  // for test
  public void updateAddressWithoutTransaction(Long id, String address){
    Pharmacy entity =pharmacyRepository.findById(id).orElse(null);

    if(Objects.isNull(entity)){
      log.error("{PharmacyRepositoryService updateAddress} not found id : {}",id);
      return;
    }

    entity.changePharmacyAddress(address);
  }

  @Transactional(readOnly = true) // readOnly 시 dirty checking 을 실행하지 않는다. => 성능 향상
  public List<Pharmacy> findAll(){
    return pharmacyRepository.findAll();
  }
}

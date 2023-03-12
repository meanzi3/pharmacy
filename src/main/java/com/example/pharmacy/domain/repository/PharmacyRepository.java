package com.example.pharmacy.domain.repository;

import com.example.pharmacy.domain.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
}

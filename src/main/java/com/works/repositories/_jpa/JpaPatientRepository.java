package com.works.repositories._jpa;

import com.works.entities.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaPatientRepository extends JpaRepository<Patient, Integer> {

    List<Patient> findByOrderByPaIdAsc(Pageable pageable);

    List<Patient> findByCustomer_CuIdEquals(Integer cuId);

    List<Patient> findBySaveDateEqualsIgnoreCase(String saveDate);

}

package com.works.repositories._jpa;

import com.works.entities.Vaccine;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaVaccineRepository extends JpaRepository<Vaccine,Integer> {

    List<Vaccine> findByOrderByVacidAsc(Pageable pageable);

}

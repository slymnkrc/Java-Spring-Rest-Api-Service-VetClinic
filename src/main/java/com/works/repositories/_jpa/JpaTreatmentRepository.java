package com.works.repositories._jpa;

import com.works.entities.Treatment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaTreatmentRepository extends JpaRepository<Treatment,Integer> {
    List<Treatment> findByOrderByTreIdAsc(Pageable pageable);



}

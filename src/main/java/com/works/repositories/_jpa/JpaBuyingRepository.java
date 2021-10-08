package com.works.repositories._jpa;

import com.works.entities.Buying;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaBuyingRepository extends JpaRepository<Buying,Integer> {
    List<Buying> findByOrderByBuyIdAsc(Pageable pageable);
}

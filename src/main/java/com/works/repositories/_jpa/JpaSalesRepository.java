package com.works.repositories._jpa;

import com.works.entities.Sales;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaSalesRepository extends JpaRepository<Sales,Integer> {
    List<Sales> findByOrderBySaIdAsc(Pageable pageable);
    List<Sales> findByCustomer_CuIdEquals(Integer cuId);
}

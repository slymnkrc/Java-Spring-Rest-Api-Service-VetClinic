package com.works.repositories._jpa;

import com.works.entities.PayOut;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaPayOutRepository extends JpaRepository<PayOut,Integer> {
    List<PayOut> findByOrderByPoIdAsc(Pageable pageable);
}

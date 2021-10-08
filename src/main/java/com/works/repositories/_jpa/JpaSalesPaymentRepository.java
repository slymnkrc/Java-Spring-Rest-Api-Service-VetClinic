package com.works.repositories._jpa;

import com.works.entities.SalesPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSalesPaymentRepository extends JpaRepository<SalesPayment,Integer> {
    SalesPayment findBySales_SaIdEquals(Integer saId);



}

package com.works.repositories._jpa;

import com.works.entities.BuyingPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBuyingPaymentRepository extends JpaRepository<BuyingPayment,Integer> {
    BuyingPayment findByBuying_BuyIdEquals(Integer buyId);
}

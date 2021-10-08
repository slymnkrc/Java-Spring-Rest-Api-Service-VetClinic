package com.works.repositories._jpa;

import com.works.entities.PayIn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPayInRepository extends JpaRepository<PayIn,Integer> {
}

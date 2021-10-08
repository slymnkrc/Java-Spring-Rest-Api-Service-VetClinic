package com.works.repositories._jpa;

import com.works.entities.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaCustomerRepository extends JpaRepository<Customer,Integer> {

    List<Customer> findByOrderByCuIdAsc(Pageable pageable);
}

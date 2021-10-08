package com.works.repositories._jpa;

import com.works.entities.Supplier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaSupplierRepository extends JpaRepository<Supplier,Integer> {

    //List<Supplier> findByOrderByUidAsc(Pageable pageable);

    List<Supplier> findByOrderBySupIdAsc(Pageable pageable);


}

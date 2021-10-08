package com.works.repositories._jpa;

import com.works.entities.SupplierProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaSupplierProductRepository extends JpaRepository<SupplierProduct,Integer> {
    List<SupplierProduct> findBySupplier_SupIdEquals(Integer supId);
}


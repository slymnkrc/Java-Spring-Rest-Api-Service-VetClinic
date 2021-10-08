package com.works.repositories._jpa;

import com.works.entities.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaProductRepository extends JpaRepository<Product,Integer> {
    List<Product> findByOrderByProIdAsc(Pageable pageable);
}

package com.works.repositories._jpa;

import com.works.entities.Storage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaStorageRepository extends JpaRepository<Storage, Integer> {

    List<Storage> findByOrderByStorIdAsc(Pageable pageable);

}

package com.works.repositories._jpa;

import com.works.entities.Cities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCitiesRepository extends JpaRepository<Cities,Integer> {
}


package com.works.repositories._jpa;

import com.works.entities.Towns;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaTownsRepository extends JpaRepository<Towns,Integer> {

    List<Towns> findByTownCityKeyEquals(Integer townCityKey);

}

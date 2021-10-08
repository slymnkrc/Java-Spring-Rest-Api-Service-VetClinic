package com.works.repositories._jpa;

import com.works.entities.AccountActivities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAccountActivityRepository extends JpaRepository<AccountActivities,Integer>{
}

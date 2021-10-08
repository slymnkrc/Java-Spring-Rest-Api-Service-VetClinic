package com.works.repositories._jpa;

import com.works.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRoleRepository extends JpaRepository<Role,Integer> {
}


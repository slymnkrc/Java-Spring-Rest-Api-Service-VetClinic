package com.works.repositories._jpa;

import com.works.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByuEmailEqualsIgnoreCase(String uEmail);

    @Query(
            value = "SELECT * FROM USER ORDER BY u_id",
            countQuery = "SELECT count(*) FROM User",
            nativeQuery = true)
    Page<User> findAllUsersWithPagination(Pageable pageable);

}
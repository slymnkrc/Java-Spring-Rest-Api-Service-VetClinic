package com.works.repositories._redis;

import com.works.models.RedisTowns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableRedisRepositories
public interface RedisTownsRepository extends CrudRepository<RedisTowns,String> {

    List<RedisTowns> findByTownCityKeyEquals(Integer townCityKey);

}


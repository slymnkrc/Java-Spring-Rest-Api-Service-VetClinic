package com.works.repositories._redis;

import com.works.models.RedisCities;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

@EnableRedisRepositories
public interface RedisCitiesRepository extends CrudRepository<RedisCities,String> {
}


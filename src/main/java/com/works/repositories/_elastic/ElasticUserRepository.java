package com.works.repositories._elastic;

import com.works.models.ElasticUser;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface ElasticUserRepository extends ElasticsearchRepository<ElasticUser,String> {

    @Query("{\"bool\":{\"must\":[{\"term\":{\"uId\":\"?0\"}}],\"must_not\":[],\"should\":[]}}")
    Optional<ElasticUser> findById(Integer uId);

}


package com.works.repositories._elastic;

import com.works.models.ElasticStorage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticStorageRepository extends ElasticsearchRepository<ElasticStorage,String> {

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"match\":{\"storName\":\"?0\"}},{\"match\":{\"storNo\":\"?0\"}},{\"match\":{\"storStatus\":\"?0\"}}]}},\"from\":0,\"size\":10,\"sort\":[],\"aggs\":{}")
    Page<ElasticStorage> findByStorage(String name, Pageable pageable);

}

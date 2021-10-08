package com.works.repositories._elastic;

import com.works.models.ElasticSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticSupplierRepository extends ElasticsearchRepository<ElasticSupplier,String> {

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"match\":{\"supEmail\":\"?0\"}},{\"match\":{\"supName\":\"?0\"}},{\"match\":{\"supPhone\":\"?0\"}}]}},\"from\":0,\"size\":10,\"sort\":[],\"aggs\":{}")
    Page<ElasticSupplier> findBySupplier(String name, Pageable pageable);
}

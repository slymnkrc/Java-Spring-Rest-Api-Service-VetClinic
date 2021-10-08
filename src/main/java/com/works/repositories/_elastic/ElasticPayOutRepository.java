package com.works.repositories._elastic;

import com.works.models.ElasticPayOut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticPayOutRepository extends ElasticsearchRepository<ElasticPayOut,String> {

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"match\":{\"buyReceiptNo\":\"?0\"}},{\"match\":{\"createdDate\":\"?0\"}},{\"match\":{\"poutAmount\":\"?0\"}},{\"match\":{\"poutNote\":\"?0\"}}]}},\"from\":0,\"size\":10,\"sort\":[],\"aggs\":{}")
    Page<ElasticPayOut> findByPayOut(String name, Pageable pageable);
}

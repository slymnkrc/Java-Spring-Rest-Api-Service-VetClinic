package com.works.repositories._elastic;

import com.works.models.ElasticBuying;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticBuyingRepository extends ElasticsearchRepository<ElasticBuying,String> {

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"match\":{\"buyDate\":\"?0\"}},{\"match\":{\"buyNote\":\"?0\"}},{\"match\":{\"buyReceiptNo\":\"?0\"}},{\"match\":{\"proName\":\"?0\"}},{\"match\":{\"supName\":\"?0\"}}]}},\"from\":0,\"size\":10,\"sort\":[],\"aggs\":{}")
    Page<ElasticBuying> findByBuying(String name, Pageable pageable);
}

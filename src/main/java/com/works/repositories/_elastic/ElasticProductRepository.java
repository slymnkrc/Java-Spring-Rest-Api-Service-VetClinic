package com.works.repositories._elastic;

import com.works.models.ElasticProduct;
import com.works.models.ElasticSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticProductRepository extends ElasticsearchRepository<ElasticProduct,String> {

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"match\":{\"proBarcode\":\"?0\"}},{\"match\":{\"proCategory\":\"?0\"}},{\"match\":{\"proCode\":\"?0\"}},{\"match\":{\"proDetail\":\"?0\"}},{\"match\":{\"proName\":\"?0\"}},{\"match\":{\"proTax\":\"?0\"}}]}},\"from\":0,\"size\":10,\"sort\":[],\"aggs\":{}")
    Page<ElasticProduct> findByProduct(String name, Pageable pageable);
}

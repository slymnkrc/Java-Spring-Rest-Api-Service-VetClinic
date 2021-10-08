package com.works.repositories._elastic;

import com.works.models.ElasticSales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticSalesRepository extends ElasticsearchRepository<ElasticSales,String> {

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"match\":{\"cuName\":\"?0\"}},{\"match\":{\"paName\":\"?0\"}},{\"match\":{\"proName\":\"?0\"}},{\"match\":{\"saLabType\":\"?0\"}},{\"match\":{\"saNote\":\"?0\"}},{\"match\":{\"saReceiptNo\":\"?0\"}},{\"match\":{\"saSoldDate\":\"?0\"}},{\"match\":{\"vacName\":\"?0\"}}]}},\"from\":0,\"size\":10,\"sort\":[],\"aggs\":{}")
    Page<ElasticSales> findBySales(String Name, Pageable pageable);

}

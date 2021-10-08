package com.works.repositories._elastic;

import com.works.models.ElasticSupplier;
import com.works.models.ElasticVaccine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticVaccineRepository extends ElasticsearchRepository<ElasticVaccine,String> {

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"match\":{\"vacBarcode\":\"?0\"}},{\"match\":{\"vacCategory\":\"?0\"}},{\"match\":{\"vacCode\":\"?0\"}},{\"match\":{\"vacDetail\":\"?0\"}},{\"match\":{\"vacName\":\"?0\"}},{\"match\":{\"vacSupplier\":\"?0\"}},{\"match\":{\"vacType\":\"?0\"}}]}},\"from\":0,\"size\":10,\"sort\":[],\"aggs\":{}")
    Page<ElasticVaccine> findByVaccine(String name, Pageable pageable);

}

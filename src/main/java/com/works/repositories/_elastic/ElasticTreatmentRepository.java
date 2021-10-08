package com.works.repositories._elastic;

import com.works.models.ElasticTreatment;
import com.works.models.ElasticVaccine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface ElasticTreatmentRepository extends ElasticsearchRepository<ElasticTreatment,String> {

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"match\":{\"cuName\":\"?0\"}},{\"match\":{\"paName\":\"?0\"}},{\"match\":{\"treCode\":\"?0\"}},{\"match\":{\"treDressing\":\"?0\"}},{\"match\":{\"treLab\":\"?0\"}},{\"match\":{\"treMedicine\":\"?0\"}},{\"match\":{\"treNote\":\"?0\"}},{\"match\":{\"treOperation\":\"?0\"}},{\"match\":{\"treRadiography\":\"?0\"}},{\"match\":{\"vacName\":\"?0\"}}]}},\"from\":0,\"size\":10,\"sort\":[],\"aggs\":{}")
    Page<ElasticTreatment> findByTreatment(String name, Pageable pageable);

}

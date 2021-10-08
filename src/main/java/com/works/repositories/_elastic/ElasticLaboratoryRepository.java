package com.works.repositories._elastic;

import com.works.models.ElasticLaboratory;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface ElasticLaboratoryRepository extends ElasticsearchRepository<ElasticLaboratory,String> {

    @Query("{\"bool\":{\"must\":[{\"term\":{\"labId\":\"?0\"}}],\"must_not\":[],\"should\":[]}}")
    Optional<ElasticLaboratory> findById(Integer labId);

}

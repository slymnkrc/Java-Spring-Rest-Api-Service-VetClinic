package com.works.repositories._elastic;

import com.works.models.ElasticPayIn;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticPayInRepository extends ElasticsearchRepository<ElasticPayIn,String> {
}

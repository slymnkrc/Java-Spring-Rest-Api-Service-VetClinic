package com.works.repositories._elastic;

import com.works.models.ElasticPatient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticPatientRepository extends ElasticsearchRepository<ElasticPatient,String> {

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"match\":{\"paAirTagNo\":\"?0\"}},{\"match\":{\"paBirthDate\":\"?0\"}},{\"match\":{\"paChipNo\":\"?0\"}},{\"match\":{\"paColor\":\"?0\"}},{\"match\":{\"paKind\":\"?0\"}},{\"match\":{\"paName\":\"?0\"}},{\"match\":{\"paSexType\":\"?0\"}},{\"match\":{\"paSpay\":\"?0\"}},{\"match\":{\"paType\":\"?0\"}},{\"match\":{\"saveDate\":\"?0\"}}]}},\"from\":0,\"size\":10,\"sort\":[],\"aggs\":{}")
    Page<ElasticPatient> findByPatient(String name, Pageable pageable);

}

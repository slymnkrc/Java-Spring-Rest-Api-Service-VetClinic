package com.works.repositories._elastic;

import com.works.models.ElasticCustomer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticCustomerRepository extends ElasticsearchRepository<ElasticCustomer,String> {

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"match\":{\"cuAddress\":\"?0\"}},{\"match\":{\"cuCity\":\"?0\"}},{\"match\":{\"cuEmail\":\"?0\"}},{\"match\":{\"cuName\":\"?0\"}},{\"match\":{\"cuNote\":\"?0\"}},{\"match\":{\"cuPhone\":\"?0\"}},{\"match\":{\"cuPhone2\":\"?0\"}},{\"match\":{\"cuSurname\":\"?0\"}},{\"match\":{\"cuTax\":\"?0\"}},{\"match\":{\"cuTaxOffice\":\"?0\"}},{\"match\":{\"cuTown\":\"?0\"}},{\"match\":{\"cuType\":\"?0\"}}]}},\"from\":0,\"size\":10,\"sort\":[],\"aggs\":{}")
    Page<ElasticCustomer> findByCustomer(String name, Pageable pageable);
}

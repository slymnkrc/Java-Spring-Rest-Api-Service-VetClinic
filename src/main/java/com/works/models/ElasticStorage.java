package com.works.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "storage")
@Data
public class ElasticStorage {

    @Id
    private String storId;

    @Field(type = FieldType.Text)
    private String storName;

    @Field(type = FieldType.Text)
    private String storNo;

    @Field(type = FieldType.Text)
    private String storStatus;

}

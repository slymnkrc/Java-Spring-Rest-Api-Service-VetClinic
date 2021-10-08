package com.works.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "supplier")
@Data
public class ElasticSupplier {

    @Id
    private String sid;

    @Field(type = FieldType.Text)
    private String supName;

    @Field(type = FieldType.Text)
    private String supEmail;

    @Field(type = FieldType.Text)
    private String supPhone;

}

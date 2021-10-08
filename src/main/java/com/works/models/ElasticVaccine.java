package com.works.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "vaccine")
@Data
public class ElasticVaccine {

    @Id
    private String vacid;

    @Field(type = FieldType.Text)
    private String vacName;

    @Field(type = FieldType.Text)
    private String vacUnit;

    @Field(type = FieldType.Text)
    private String vacCategory;

    @Field(type = FieldType.Text)
    private String vacDetail;

    @Field(type = FieldType.Text)
    private String vacType;

    @Field(type = FieldType.Text)
    private String vacSupplier;

    @Field(type = FieldType.Text)
    private String  vacBarcode;

    @Field(type = FieldType.Text)
    private String vacCode;

}

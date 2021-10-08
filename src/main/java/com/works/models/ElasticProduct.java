package com.works.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "product")
@Data
public class ElasticProduct {

    @Id
    private String pid;

    @Field(type = FieldType.Text)
    private String proName;

    @Field(type = FieldType.Text)
    private String proUnit;

    @Field(type = FieldType.Text)
    private String proCategory;

    @Field(type = FieldType.Text)
    private String proDetail;

    @Field(type = FieldType.Text)
    private String proType;

    @Field(type = FieldType.Text)
    private String proSupplier;

    @Field(type = FieldType.Text)
    private String proBarcode;

    @Field(type = FieldType.Text)
    private String proCode;

    @Field(type = FieldType.Text)
    private String proTax;


}

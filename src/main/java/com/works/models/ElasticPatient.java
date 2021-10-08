package com.works.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "patient")
@Data
public class ElasticPatient {

    @Id
    private String paId;

    @Field(type = FieldType.Text)
    private String customerId;

    @Field(type = FieldType.Text)
    private String paName;

    @Field(type = FieldType.Text)
    private String paChipNo;

    @Field(type = FieldType.Text)
    private String paAirTagNo;

    @Field(type = FieldType.Text)
    private String paBirthDate;

    @Field(type = FieldType.Text)
    private String paType;

    @Field(type = FieldType.Text)
    private String paSpay;

    @Field(type = FieldType.Text)
    private String paKind;

    @Field(type = FieldType.Text)
    private String paColor;

    @Field(type = FieldType.Text)
    private String paSexType;

    @Field(type = FieldType.Text)
    private String saveDate;


}

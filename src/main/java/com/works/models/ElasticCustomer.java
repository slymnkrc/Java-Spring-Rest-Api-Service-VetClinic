package com.works.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "customer")
@Data
public class ElasticCustomer {

    @Id
    private String cuId;

    @Field(type = FieldType.Text)
    private String cuName;

    @Field(type = FieldType.Text)
    private String cuSurname;

    @Field(type = FieldType.Text)
    private String cuTax;

    @Field(type = FieldType.Text)
    private String cuTaxOffice;

    @Field(type = FieldType.Text)
    private String cuPhone;

    @Field(type = FieldType.Text)
    private String cuPhone2;

    @Field(type = FieldType.Text)
    private String cuEmail;

    @Field(type = FieldType.Text)
    private String cuType;

    @Field(type = FieldType.Text)
    private String cuCity;

    @Field(type = FieldType.Text)
    private String cuTown;

    @Field(type = FieldType.Text)
    private String cuAddress;

    @Field(type = FieldType.Text)
    private String cuNote;

}

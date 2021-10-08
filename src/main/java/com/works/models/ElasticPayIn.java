package com.works.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "payin")
@Data
public class ElasticPayIn {

    @Id
    private String pinId;

    @Field(type = FieldType.Text)
    private String pinNote;

    @Field(type = FieldType.Text)
    private String pinAmount;

    @Field(type = FieldType.Text)
    private String pinPayType;

    @Field(type = FieldType.Text)
    private String createDate;

    @Field(type = FieldType.Text)
    private String saReceiptNo;

    @Field(type = FieldType.Text)
    private String invoice;

    @Field(type = FieldType.Text)
    private String cuname;


}

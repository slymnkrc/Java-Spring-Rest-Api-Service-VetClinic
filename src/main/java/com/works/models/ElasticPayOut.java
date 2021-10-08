package com.works.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "payout")
@Data
public class ElasticPayOut {

    @Id
    private String poId;

    @Field(type = FieldType.Text)
    private String poutNote;

    @Field(type = FieldType.Text)
    private String poutAmount;

    @Field(type = FieldType.Text)
    private String createdDate;

    @Field(type = FieldType.Text)
    private String buyReceiptNo;
}

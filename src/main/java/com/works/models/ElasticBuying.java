package com.works.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "buying")
@Data
public class ElasticBuying {

    @Id
    private String buyId;

    @Field(type = FieldType.Text)
    private String buyReceiptNo;

    @Field(type = FieldType.Text)
    private String buyNote;

    @Field(type = FieldType.Text)
    private String buyDate;

    @Field(type = FieldType.Text)
    private String supName;

    @Field(type = FieldType.Text)
    private String proName;

}
